public class Command{
    public String command;
    public String path;
    public String value;
    public Command(String command, String path, String value){
        this.command = command;
        this.path = path;
        this.value = value;
    }
    public Command(){
        this("", "", "");
    }
}
