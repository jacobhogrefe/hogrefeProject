package Scripts.BiomeSpooky;

import Level.NPC;
import Level.Script;
import Level.ScriptState;

// script for talking to mushroom man npc
// the script is segmented -- it has multiple setups, cleanups, and executions based on its current action
public class CoralineScript extends Script<NPC> {

	@Override
	protected void setup() {
		lockPlayer();
		showTextbox();
		if(!isFlagSet("firstTalktoCoraline")) {
			addTextToTextboxQueue("Hello, lost one! I need help finding the lost children's \neyes!");
			addTextToTextboxQueue("The other mother took them after she sewed buttons \ninto their eyes and hid them around this biome!");
			addTextToTextboxQueue("The other mother's hint was that she hid the eyes in \neach wonder in this world.");
			addTextToTextboxQueue("Have all three eyes collected before coming back \nto me!");
		} else if(isFlagSet("FoundtheLostEyes")) {
			addTextToTextboxQueue("Thank you lost one for helping me \nfree the lost children!");
			addTextToTextboxQueue("Now I can go back to my world and defeat \nthe other mother and find my parents!");
			addTextToTextboxQueue("Goodluck on the rest of your journey lost \none! Here this is for all your help!");
		} else {
			addTextToTextboxQueue("I wonder where the other mother is right now.");
		}
		entity.facePlayer(player);
	}

	@Override
	protected void cleanup() {
		// TODO Auto-generated method stub
		unlockPlayer();
		hideTextbox();
		if(!isFlagSet("firstTalktoCoraline")) {
			setFlag("firstTalktoCoraline");
//			unsetFlag("Searchlosteyes");
		} else if (isFlagSet("FoundtheLostEyes")) {
//			unsetFlag("FoundtheLostEyes");
			
			setFlag("giveHeadphones");
			setFlag("removeItem7");
		}

	}

	@Override
	protected ScriptState execute() {
		// TODO Auto-generated method stub
		start();
		if(!isTextboxQueueEmpty()) {
			return ScriptState.RUNNING;
		}
		end();
		return ScriptState.COMPLETED;
	}
}