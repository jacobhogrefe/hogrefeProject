package Scripts.BiomeMountains;

import Level.NPC;
import Level.Script;
import Level.ScriptState;

public class MarioScript extends Script<NPC> {

    @Override
    protected void setup() {
        lockPlayer();
        showTextbox();
        if (!isFlagSet("firstTalkToMario")) {
            addTextToTextboxQueue("It's-a me, Mario! I've lost some things around this area.");
            addTextToTextboxQueue("Do you think you can help me find them? I'll reward\nyou too!");
            addTextToTextboxQueue("One thing I remember losing is my Nintendo Switch.");
            addTextToTextboxQueue("It should be somewhere on one of the mountains.");
        } else if (!isFlagSet("searchForSwitch")) {
            addTextToTextboxQueue("One thing I remember losing is my Nintendo Switch.");
            addTextToTextboxQueue("It should be somewhere on one of the mountains.");
        } else if (isFlagSet("foundSwitch")) {
            addTextToTextboxQueue("Thank you! Now I can play Super Mario Odyssey!");
            addTextToTextboxQueue("Here is an entertainment center for your home!");
            addTextToTextboxQueue("Any instant food up here is a necessity.");
            addTextToTextboxQueue("Do you think you can bring me some ramen?");
            addTextToTextboxQueue("I think I left it somewhere around here.");
        } else if (!isFlagSet("searchForRamen")) {
            addTextToTextboxQueue("Any instant food up here is a necessity.");
            addTextToTextboxQueue("Do you think you can bring me some ramen?");
            addTextToTextboxQueue("I think I left it somewhere around here.");
        } else if (isFlagSet("foundRamen")) {
            addTextToTextboxQueue("Oooo yummy! I know this is a staple in a college\nstudent's diet!");
            addTextToTextboxQueue("I think this dorm furniture will do you well!");
            addTextToTextboxQueue("Being made up of 1's and 0's really does give you\nan existential crisis.");
            addTextToTextboxQueue("But, I want to make my own 1's and 0's!");
            addTextToTextboxQueue("Can you bring me a terminal? I believe it's lower\non the ground.");
        } else if (!isFlagSet("searchForTerminal")) {
            addTextToTextboxQueue("Being made up of 1's and 0's really does give you\nan existential crisis.");
            addTextToTextboxQueue("But, I want to make my own 1's and 0's!");
            addTextToTextboxQueue("Can you bring me a terminal? I believe it's lower\non the ground.");
        } else if (isFlagSet("foundTerminal")) {
            addTextToTextboxQueue("Thank you so much! Now I can make my own game!");
            addTextToTextboxQueue("Since you look like a computer science student,\nhere is a desk!");
            addTextToTextboxQueue("I have one more item hidden around here. It's a coin\nfrom the 90s.");
            addTextToTextboxQueue("I'm not sure where I last saw it, but it is\npretty valuable.");
        } else if (!isFlagSet("searchForYoshiCoin")) {
            addTextToTextboxQueue("I have one more item hidden around here. It's a coin\nfrom the 90s.");
            addTextToTextboxQueue("I'm not sure where I last saw it, but it is\npretty valuable.");
        } else if (isFlagSet("foundYoshiCoin")) {
            addTextToTextboxQueue("Perfect! This was exactly what I was looking for!");
            addTextToTextboxQueue("Here is a hawaiian shirt for a certain blue\nfella. Tell him Mario says hi!");
        } else {
            addTextToTextboxQueue("It's pretty chilly up here, maybe you should invest\nin a jacket.");
        }
        entity.facePlayer(player);
    }

    @Override
    protected void cleanup() {
        unlockPlayer();
        hideTextbox();
        if (!isFlagSet("firstTalkToMario")) {
            setFlag("firstTalkToMario");
            unsetFlag("searchForSwitch");
        } else if (isFlagSet("searchForSwitch") && isFlagSet("foundSwitch")) {
            unsetFlag("foundSwitch");
            unsetFlag("searchForRamen");
            setFlag("removeItem");
        } else if (isFlagSet("searchForRamen") && isFlagSet("foundRamen")) {
            unsetFlag("foundRamen");
            unsetFlag("searchForTerminal");
            setFlag("removeItem");
        } else if (isFlagSet("searchForTerminal") && isFlagSet("foundTerminal")) {
            unsetFlag("foundTerminal");
            unsetFlag("searchForYoshiCoin");
            setFlag("removeItem");
        } else if (isFlagSet("searchForYoshiCoin") && isFlagSet("foundYoshiCoin")) {
            unsetFlag("foundYoshiCoin");
            setFlag("removeItem");
        }
    }

    @Override
    protected ScriptState execute() {
        start();
        if (!isTextboxQueueEmpty()) {
            return ScriptState.RUNNING;
        }
        end();
        return ScriptState.COMPLETED;
    }
}
