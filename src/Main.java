import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.transport.OSCPortOut;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class Main extends javax.swing.JFrame{
    private boolean exists;
    private LiveSpeechRecognizer recognizer;
    private boolean running;
    private OSCPortOut osc;
    private ArrayList<String> history = new ArrayList<>();
    private ArrayList<Command> commands = new ArrayList<>();
    public Main(){
        initComponents();
        if(new File("commands.txt").exists()){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("commands.txt"))))){
                READ:while(true){
                    String line = reader.readLine();
                    String[] args = line.split(" ");
                    switch(args[0]){
                        case "COMMANDS":
                            int count = Integer.parseInt(args[1]);
                            for(int i = 0; i<count; i++){
                                String[] cmd = reader.readLine().split("\\|");
                                addCommand(new Command(cmd[0], cmd[1], cmd[2]));
                            }
                            break;
                        case "END":
                            break READ;
                    }
                }
            }catch(IOException ex){
                JOptionPane.showMessageDialog(this, Arrays.toString(ex.getStackTrace()).replace(",", "\n"), ex.getClass().getName()+": "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
        start();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelStatus = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaOutput = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelCommands = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        buttonAddCommand = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SpeechToOSC v1.0.0");

        labelStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelStatus.setText("INITIALIZING");
        getContentPane().add(labelStatus, java.awt.BorderLayout.PAGE_START);

        textAreaOutput.setColumns(20);
        textAreaOutput.setRows(5);
        jScrollPane1.setViewportView(textAreaOutput);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.PAGE_END);

        jPanel1.setLayout(new java.awt.BorderLayout());

        panelCommands.setLayout(new java.awt.GridLayout(0, 4));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Voice Command");
        panelCommands.add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("OSC Path");
        panelCommands.add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Value");
        panelCommands.add(jLabel3);
        panelCommands.add(jLabel4);

        jScrollPane2.setViewportView(panelCommands);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(0, 1));

        buttonAddCommand.setText("Add Command");
        buttonAddCommand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddCommandActionPerformed(evt);
            }
        });
        jPanel2.add(buttonAddCommand);

        buttonSave.setText("Save Configuration");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });
        jPanel2.add(buttonSave);

        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void buttonAddCommandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAddCommandActionPerformed
        addCommand(new Command());
    }//GEN-LAST:event_buttonAddCommandActionPerformed
    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("commands.txt"))))){
            writer.write("COMMANDS "+commands.size()+"\n");
            for(Command command : commands){
                writer.write(command.command+"|"+command.path+"|"+command.value+"\n");
            }
            writer.write("END\n");
        }catch(IOException ex){
            JOptionPane.showMessageDialog(this, Arrays.toString(ex.getStackTrace()).replace(",", "\n"), ex.getClass().getName()+": "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonSaveActionPerformed
    private void addCommand(Command command){
        commands.add(command);
        JTextField commandField = new JTextField(command.command);
        panelCommands.add(commandField);
        commandField.addActionListener((e) -> {
            command.command = commandField.getText();
        });
        JTextField pathField = new JTextField(command.path);
        panelCommands.add(pathField);
        pathField.addActionListener((e) -> {
            command.path = pathField.getText();
        });
        JTextField valueField = new JTextField(command.value);
        panelCommands.add(valueField);
        valueField.addActionListener((e) -> {
            command.value = valueField.getText();
        });
        JButton deleteButton = new JButton("Delete");
        panelCommands.add(deleteButton);
        deleteButton.addActionListener((e) -> {
            commands.remove(command);
            panelCommands.remove(commandField);
            panelCommands.remove(pathField);
            panelCommands.remove(valueField);
            panelCommands.remove(deleteButton);
            revalidate();
            repaint();
        });
        revalidate();
        repaint();
    }
    public void start(){
        if(!exists){
            try{
                osc = new OSCPortOut(InetAddress.getByName("127.0.0.1"), 9000);
                Configuration config = new Configuration();
                config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
                config.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
                config.setGrammarPath("grammar");
                config.setGrammarName("grammar");
                config.setUseGrammar(true);
                recognizer = new LiveSpeechRecognizer(config);
                exists = true;
            }catch(IOException ex){
                JOptionPane.showMessageDialog(this, Arrays.toString(ex.getStackTrace()).replace(",", "\n"), ex.getClass().getName()+": "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
        running = !running;
        if(running){
            new Thread(() -> {
                try{
                    recognizer.startRecognition(true);
                }catch(RuntimeException ex){
                    JOptionPane.showMessageDialog(this, "Failed to start speech recognition. (Probably a grammar parsing error)");
                    dispose();
                    return;
                }
                labelStatus.setText("ACTIVE");
                while(running){
                    SpeechResult result = recognizer.getResult();
                    if(result!=null){
                        processSpeech(result.getHypothesis(), result.getWords());
                    }
                }
                labelStatus.setText("STOPPED");
                recognizer.stopRecognition();
            }).start();
        }
    }
    public static void main(String args[]){
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try{
            for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()){
                if("Nimbus".equals(info.getName())){
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(ClassNotFoundException ex){
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }catch(InstantiationException ex){
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }catch(IllegalAccessException ex){
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }catch(javax.swing.UnsupportedLookAndFeelException ex){
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run(){
                new Main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAddCommand;
    private javax.swing.JButton buttonSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelStatus;
    private javax.swing.JPanel panelCommands;
    private javax.swing.JTextArea textAreaOutput;
    // End of variables declaration//GEN-END:variables
    private void processSpeech(String hypothesis, List<WordResult> results){
        history.add(hypothesis);
        while(history.size()>5)history.remove(0);
        textAreaOutput.setText(String.join("\n", history));
        for(Command command : commands){
            if(hypothesis.matches(command.command)){
                Object value = command.value;
                if(command.value.equalsIgnoreCase("true"))value = true;
                if(command.value.equalsIgnoreCase("false"))value = false;
                try{
                    value = Integer.valueOf(command.value);
                }catch(NumberFormatException ex){}
                try{
                    value = Float.valueOf(command.value);
                }catch(NumberFormatException ex){}
                if(command.value.equalsIgnoreCase("toggle"))value = toggle(command.path);
                if(value instanceof Boolean)toggles.put(command.path, (Boolean)value);
                try{
                    osc.send(new OSCMessage(command.path, Arrays.asList(value)));
                }catch(IOException|OSCSerializeException ex){
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    public HashMap<String, Boolean> toggles = new HashMap<>();
    private boolean toggle(String param){
        toggles.put(param, !toggles.getOrDefault(param, false));
        return toggles.get(param);
    }
}
