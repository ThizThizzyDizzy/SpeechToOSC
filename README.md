# Usage

Download the jar file from releases, as well as the `lib` and `grammar` folders from the repository. Configure the grammar file and run the jar file.

## Grammar

It will try to match anything you say to the patterns defined in the grammar file, so it's a good idea to have some short nonsense words to catch random sounds. (It *will* try to interpret a grunt into a full sentence if you don't)

Anything it hears is printed in the box below; <unk> means unknown

### Grammar File

The grammar file is `grammar.gram` under the `grammar` folder. For all intents and purposes, it follows minecraft command syntax.

That is, `literal words <variable> [optional words] (any|one|of|these)`

If special words (names, etc.) give you trouble, try to match them with simple words phonetically, it does not have a very big dictionary.

Extreme detail on the grammar format can be found [Here](<https://www.w3.org/TR/2000/NOTE-jsgf-20000605/#14185>)

(To reload the grammar file, restart the program)

## SpeechToOSC Interface

Voice commands must match the entire phrase that is heard (one line in the output). these do support regex if you feel like going completely insane

values will be interpreted as integers if possible, (include a decimal point for floats)

`true` or `false` are converted to bools, and `toggle` will alternate between them

**All three command fields only apply changes when you press enter.**

There is also no autosave, so make sure you press save :p
