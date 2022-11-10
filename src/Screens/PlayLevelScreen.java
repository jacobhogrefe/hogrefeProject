package Screens;

import java.util.Stack;
import java.awt.Color;
import Engine.GlobalKeyCooldown;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.ScreenCoordinator;
import GameObject.SpriteSheet;
import Level.*;
import Maps.Biomes.BiomeSpooky;
import Maps.HouseMap;
import Maps.Biomes.BiomeStart;
import NPCs.Cloud;
import NPCs.Cloud2;
import NPCs.Cloud3;
import Players.Cat;
import SpriteFont.SpriteFont;
import Utils.Direction;
import Utils.Point;
import Engine.Clock;
import Engine.Config;

// This class is for when the RPG game is actually being played
public class PlayLevelScreen extends Screen {
	protected ScreenCoordinator screenCoordinator;
	protected Map map;
	protected Player player;
	protected PlayLevelScreenState playLevelScreenState;
	protected WinScreen winScreen;
	protected InventoryScreen inventoryScreen;
	protected FlagManager flagManager;
	protected KeyLocker keyLocker = new KeyLocker();
	protected PlayerInventory playerInventory = new PlayerInventory();
	protected Clock clock = new Clock();
	protected SpriteFont time = new SpriteFont("Time: " + clock.getTimeOfDay() + ":00", 10, 30, "Comic Sans", 30,
			Color.BLACK);
	protected Cloud cloud = new Cloud(0, new Point(570, 4));
	protected Cloud2 cloud2 = new Cloud2(0, new Point(235, 10));
	protected Cloud3 cloud3 = new Cloud3(0, new Point(0, 35));
	protected MusicManager musicManager = new MusicManager();
	protected Key Inventory_Key = Key.I;
	protected Key Pause_Key = Key.P;
	protected Key Debug_Key = Key.ZERO;
	protected boolean isInventoryOpen = false;
	public static boolean isInHouse = false;
	public static boolean shouldcensorwalrus = false;

	public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
		this.screenCoordinator = screenCoordinator;
	}

	public void initialize() {
		// setup state
		flagManager = new FlagManager();
		flagManager.addFlag("hasCrash", false);
		flagManager.addFlag("hasTalkedToWalrus", false);
		flagManager.addFlag("itemCollected", false);
		flagManager.addFlag("hasGlasses", false);
		flagManager.addFlag("finishGlasses", false);
		flagManager.addFlag("hasTalkedToDino", false);
		flagManager.addFlag("hasMagnifying", false);
		flagManager.addFlag("noMore", false);
		flagManager.addFlag("finesse", false);
		
//		flagManager.addFlag("hasLostBall", false);
//		flagManager.addFlag("hasTalkedToDinosaur", false);
//		flagManager.addFlag("hasFoundBall", false);

		//mountains map flags
		flagManager.addFlag("removeItem", false);
		flagManager.addFlag("firstTalkToMario", false);
		flagManager.addFlag("searchForSwitch", true);
		flagManager.addFlag("foundSwitch", false);
		flagManager.addFlag("searchForRamen", true);
		flagManager.addFlag("foundRamen", false);
		flagManager.addFlag("searchForTerminal", true);
		flagManager.addFlag("foundTerminal", false);
		flagManager.addFlag("searchForYoshiCoin", true);
		flagManager.addFlag("foundYoshiCoin", false);
		
		//desert map flags
		flagManager.addFlag("needsFindBucket",false);
		flagManager.addFlag("firstBartenderTalk",false);
		flagManager.addFlag("bucketFound",false);
		flagManager.addFlag("lassoFound", false);
		flagManager.addFlag("desertReward",false);

		// define/setup map
		this.map = new HouseMap();
		// map.reset();
		map.setFlagManager(flagManager);

		// setup player
		this.player = new Cat(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
		this.musicManager.setPlayer(this.player);
		this.player.setWalkingSound(musicManager.getWalkingSound());
		this.player.setMap(map);
		Point playerStartPosition = map.getPlayerStartPosition();
		this.player.setLocation(playerStartPosition.x, playerStartPosition.y);
		this.playLevelScreenState = PlayLevelScreenState.RUNNING;
		this.player.setFacingDirection(Direction.LEFT);

		this.reinitializeMap();

		winScreen = new WinScreen(this);
		inventoryScreen = new InventoryScreen(this, playerInventory);
		musicManager.setMusicState(map.getMusicState());
		musicManager.getCurrentSound().play();
	}

	public void reinitializeMap() {
		// let pieces of map know which button to listen for as the "interact" button
		map.getTextbox().setInteractKey(player.getInteractKey());

		//sets the new map's flag manager
		map.setFlagManager(flagManager);

		// setup map scripts to have references to the map and player
		for (MapTile mapTile : map.getMapTiles()) {
			if (mapTile.getInteractScript() != null) {
				mapTile.getInteractScript().setMap(map);
				mapTile.getInteractScript().setPlayer(player);
			}
		}
		for (NPC npc : map.getNPCs()) {
			if (npc.getInteractScript() != null) {
				npc.getInteractScript().setMap(map);
				npc.getInteractScript().setPlayer(player);
			}
		}
		for (EnhancedMapTile enhancedMapTile : map.getEnhancedMapTiles()) {
			if (enhancedMapTile.getInteractScript() != null) {
				enhancedMapTile.getInteractScript().setMap(map);
				enhancedMapTile.getInteractScript().setPlayer(player);
			}
		}
		for (Trigger trigger : map.getTriggers()) {
			if (trigger.getTriggerScript() != null) {
				trigger.getTriggerScript().setMap(map);
				trigger.getTriggerScript().setPlayer(player);
			}
		}
		for (Collectible collectibles : map.getCollectables()) {
			if (collectibles.getInteractScript() == null) {
				collectibles.setScriptMusicManager(musicManager);
				collectibles.getInteractScript().setMap(map);
				collectibles.getInteractScript().setPlayer(player);
			}
		}
	}

	public void update() {
		// updates the music based on location
		
		if (CatWardrobe.wardrobeChange == true) {
			
			System.out.println("Changing clothes");
			CatWardrobe catWardrobe = new CatWardrobe();
			reloadPlayer(player);
			
			CatWardrobe.wardrobeChange = false;
		}
		
		musicManager.updateMusic();
		// based on screen state, perform specific actions
		// if level is "running" update player and map to keep game logic for the
		// platformer level going
		switch (playLevelScreenState) {
		case RUNNING:
			player.update();
			map.update(player);
			break;
		// if level has been completed, bring up level cleared screen
		case LEVEL_COMPLETED:
			winScreen.update();
			break;
		case INVENTORY_OPEN:
			inventoryScreen.update();
			break;
		}

		// if flag is set at any point during gameplay, game is "won"
		if (map.getFlagManager().isFlagSet("hasFoundBall")) {
			playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
		}

		if (Keyboard.isKeyDown(Inventory_Key) && !keyLocker.isKeyLocked(Inventory_Key)) {
			playLevelScreenState = PlayLevelScreenState.INVENTORY_OPEN;
		}
		if (Keyboard.isKeyDown(Pause_Key) && !keyLocker.isKeyLocked(Pause_Key)) {
			this.pause();
		}

		if (GlobalKeyCooldown.Keys.ZERO.onceDown()) {
			this.screenCoordinator.push(new DebugMenuScreen(this.screenCoordinator));
		}

		if (map.getFlagManager().isFlagSet("itemCollected")) {
			Stack<Integer> itemsReceived = new Stack<Integer>();

			itemsReceived = map.takeItems();

			while (!itemsReceived.empty()) {
				playerInventory.addItem(itemsReceived.pop());
			}

			inventoryScreen.setPlayerInventory(playerInventory);
			map.getFlagManager().unsetFlag("itemCollected");

		}
		if (map.getMapFileName().equals("walrus_house_map.txt")) {
			if (playerInventory.containsItem(18)) {
				flagManager.setFlag("hasGlasses");
			}
		}
		
		if (map.getMapFileName().equals("Biomes/start.txt")) {
			if (flagManager.isFlagSet("finesse")) {
				screenCoordinator.getPlayLevelScreen().getPlayerInventory().addItem(5);
				flagManager.unsetFlag("finesse");
			}
		}
		
		if(map.getMapFileName().equals("Biomes/mountains.txt")) {
			if (playerInventory.containsItem(13)) {
				flagManager.setFlag("foundSwitch");
				if (flagManager.isFlagSet("removeItem")) {
					playerInventory.removeItem(playerInventory.getItemSlotNumber(13));
					playerInventory.addItem(14);
					flagManager.unsetFlag("foundSwitch");
					flagManager.unsetFlag("removeItem");
				}
			} else if (playerInventory.containsItem(12)) {
				flagManager.setFlag("foundRamen");
				if (flagManager.isFlagSet("removeItem")) {
					playerInventory.removeItem(playerInventory.getItemSlotNumber(12));
					playerInventory.addItem(15);
					flagManager.unsetFlag("foundRamen");
					flagManager.unsetFlag("removeItem");
				}
			} else if (playerInventory.containsItem(11)) {
				flagManager.setFlag("foundTerminal");
				if (flagManager.isFlagSet("removeItem")) {
					playerInventory.removeItem(playerInventory.getItemSlotNumber(11));
					playerInventory.addItem(16);
					flagManager.unsetFlag("foundTerminal");
					flagManager.unsetFlag("removeItem");
				}
			} else if (playerInventory.containsItem(2)) {
				flagManager.setFlag("foundYoshiCoin");
				if (flagManager.isFlagSet("removeItem")) {
					playerInventory.removeItem(playerInventory.getItemSlotNumber(2));
					playerInventory.addItem(17);
					flagManager.unsetFlag("foundYoshiCoin");
					flagManager.unsetFlag("removeItem");
				}
			}
		}
	}

	public PlayerInventory getPlayerInventory() {
		return playerInventory;
	}

	public void setPlayerInventory(PlayerInventory playerInventory) {
		this.playerInventory = playerInventory;
	}

	public void draw(GraphicsHandler graphicsHandler) {
		// based on screen state, draw appropriate graphics
		switch (playLevelScreenState) {
		case RUNNING:
			playerInventory = inventoryScreen.getPlayerInventory();
			map.draw(player, graphicsHandler);
			drawTimeAndNight(graphicsHandler);
			break;
		case LEVEL_COMPLETED:
			winScreen.draw(graphicsHandler);
			break;
		case INVENTORY_OPEN:
			InventoryScreen.inventoryOpen = true;
			inventoryScreen.draw(graphicsHandler);
			break;
		}
	}

	// times can be altered from their original values
	public void drawTimeAndNight(GraphicsHandler graphicsHandler) {
		time.setText("Time: " + clock.getTimeOfDay() + ":00");
		time.draw(graphicsHandler);
		int timeOfDay = clock.getTimeOfDay();
		if (map.getMapFileName().equals("Biomes/start.txt") || 
			map.getMapFileName().equals("Biomes/desert.txt") ||
			map.getMapFileName().equals("Biomes/mountains.txt") || 
			map.getMapFileName().equals("Biomes/fallout.txt") || 
			map.getMapFileName().equals("Biomes/shrooms.txt")) {
		
			if (timeOfDay == 5 || timeOfDay == 19) {
				graphicsHandler.drawFilledRectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT,
						new Color(0, 0, 0, 25));
			} else if (timeOfDay == 4 || timeOfDay == 20) {
				graphicsHandler.drawFilledRectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT,
						new Color(0, 0, 0, 50));
			} else if (timeOfDay == 3 || timeOfDay == 21) {
				graphicsHandler.drawFilledRectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT,
						new Color(0, 0, 0, 75));
			} else if (timeOfDay == 2 || timeOfDay == 22) {
				graphicsHandler.drawFilledRectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT,
						new Color(0, 0, 0, 100));
			} else if (timeOfDay == 1 || timeOfDay == 23) {
				graphicsHandler.drawFilledRectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT,
						new Color(0, 0, 0, 125));
			} else if (timeOfDay == 24) {
				graphicsHandler.drawFilledRectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT,
						new Color(0, 0, 0, 150));
			} else {
				graphicsHandler.drawFilledRectangle(0, 0, Config.GAME_WINDOW_WIDTH, Config.GAME_WINDOW_HEIGHT,
						new Color(0, 0, 0, 0));
			}
		}
				
		if (clock.getTimeOfDay() >= 19 || clock.getTimeOfDay() <= 5) {
			if (map.getMapFileName().equals("Biomes/start.txt")) {
				cloud.draw(graphicsHandler);
				cloud2.draw(graphicsHandler);
				cloud3.draw(graphicsHandler);
				
			} 		
		}

	}

	public PlayLevelScreenState getPlayLevelScreenState() {
		return playLevelScreenState;
	}

	public boolean inventoryIsOpen() {
		return isInventoryOpen;
	}

	public void closeInventory() {
		isInventoryOpen = false;
	}

	public void pause() {
		musicManager.getCurrentSound().pause();
		screenCoordinator.push(new PauseScreen(this, screenCoordinator));
	}

	public void controls() {
		musicManager.getCurrentSound().pause();
		screenCoordinator.push(new ControlsScreen(screenCoordinator));
	}

	public void resumeLevel() {
		this.screenCoordinator.resumeLevel();
		musicManager.getCurrentSound().play();
		playLevelScreenState = PlayLevelScreenState.RUNNING;
	}

	public void resetLevel() {
		initialize();
	}
	
	public void reloadPlayer(Player currentPlayer) {
		
		float currentX = player.getX();
		float currentY = player.getY();
		
		Direction currentDirection = player.getFacingDirection();
		
		
		
		this.player = new Cat(currentX, currentY);
		this.musicManager.setPlayer(this.player);
		this.player.setWalkingSound(musicManager.getWalkingSound());
		this.player.setMap(map);
		this.player.setLocation(currentX, currentY);
		this.playLevelScreenState = PlayLevelScreenState.RUNNING;
		this.player.setFacingDirection(currentDirection);
		
	}

	public void goBackToMenu() {
		musicManager.getCurrentSound().stop();
		screenCoordinator.pop(this);
	}

	// This enum represents the different states this screen can be in
	private enum PlayLevelScreenState {
		RUNNING, LEVEL_COMPLETED, INVENTORY_OPEN
	}

	/**
	 * Teleport to a new map at a specified position.
	 * 
	 * @param map the new map
	 * @param x   the player x
	 * @param y   the player y
	 */
	public void teleport(Map map, float x, float y) {
		map.setFlagManager(this.flagManager);
		this.map = map;
		this.player.setMap(map);
		this.player.setX(x);
		this.player.setY(y);
		this.musicManager.setMusicState(map.getMusicState());
		this.reinitializeMap();
		this.map.update(this.player);
	}
}