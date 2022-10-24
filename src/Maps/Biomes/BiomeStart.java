package Maps.Biomes;

import java.util.ArrayList;
import java.util.function.Supplier;
import EnhancedMapTiles.Rock;
import Level.Collectible;
import Level.EnhancedMapTile;
import Level.Map;
import Level.MusicState;
import Level.NPC;
import Level.Trigger;
import Maps.AbstractLoopingMap;
import NPCs.Dinosaur;
import NPCs.Walrus;
import Registry.ItemRegistry;
import Registry.ItemRegistry.Item;
import Scripts.SimpleTextScript;
import Scripts.TestMap.DinoScript;
import Scripts.TestMap.EnterDinoHouseScript;
import Scripts.TestMap.EnterHouseScript;
import Scripts.TestMap.EnterWalrusHouseScript;
import Scripts.TestMap.LostBallScript;
import Scripts.TestMap.TreeScript;
import Scripts.TestMap.WalrusScript;
import Tilesets.CommonTileset;
import Utils.Side;

/**
 * Layout:
 * 
 * +------------+------------+------------+
 * |            |            |            |
 * |  Fallout   <   Spooky   >  Mountains |
 * |            |            |            |
 * +-----^------+-----^------+------^-----+
 * |            |            |            |
 * |  Shrooms   <   Start    >   Desert   |
 * |            |            |            |
 * +------------+------------+------------+
 */
public class BiomeStart extends AbstractLoopingMap {
    public static final Item REQUIRED_ITEM = null;
    private static final Supplier<Map> HouseMap = null;

    public BiomeStart() {
        super("Biomes/start.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }

    @Override
    public Supplier<Map> getBorderingMap(Side edge) {
        switch (edge) {
            case LEFT:
                return () -> new BiomeShrooms();
            case RIGHT:
                return () -> new BiomeDesert();
            case TOP:
                return () -> new BiomeSpooky();
            case BOTTOM:
                return null;
            default:
                return null;
        }
    }

    @Override
    public Item getRequiredItem(Side edge) {
        switch (edge) {
            case LEFT:
                return BiomeShrooms.REQUIRED_ITEM;
            case RIGHT:
                return BiomeDesert.REQUIRED_ITEM;
            case TOP:
                return BiomeSpooky.REQUIRED_ITEM;
            case BOTTOM:
                return null;
            default:
                return null;
        }
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        enhancedMapTiles.add(new Rock(getMapTile(2, 7).getLocation()));
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        Walrus walrus = new Walrus(1, getMapTile(4, 28).getLocation().subtractY(40));
        walrus.setInteractScript(new WalrusScript());
        npcs.add(walrus);

        Dinosaur dinosaur = new Dinosaur(2, getMapTile(13, 4).getLocation());
        dinosaur.setExistenceFlag("hasTalkedToDinosaur");
        dinosaur.setInteractScript(new DinoScript());
        npcs.add(dinosaur);

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        triggers.add(new Trigger(790, 1030, 100, 10, new LostBallScript(), "hasLostBall"));
        triggers.add(new Trigger(790, 960, 10, 80, new LostBallScript(), "hasLostBall"));
        triggers.add(new Trigger(890, 960, 10, 80, new LostBallScript(), "hasLostBall"));
        return triggers;
    }

    @Override
    public ArrayList<Collectible> loadCollectables() {
        ArrayList<Collectible> collectibles = new ArrayList<>();
        collectibles.add(new Collectible("yoshiCoin.png", getMapTile(12,19).getLocation(), "Yoshi Coin", 2, false));
        collectibles.add(new Collectible("yoshiCoin.png", getMapTile(11,19).getLocation(), "Yoshi Coin", 2, false));
        collectibles.add(new Collectible("yoshiCoin.png", getMapTile(10,19).getLocation(), "Yoshi Coin", 2, true));
        collectibles.add(new Collectible("yoshiCoin.png", getMapTile(9,19).getLocation(), "Yoshi Coin", 2, true));
        return collectibles;
    }
    
//    @Override
//    public ArrayList<HouseEntry> loadHouseEntries() {
//    	ArrayList<HouseEntry> entries = new ArrayList<>();
//    	entries.add(new HouseEntry(17, 19));
//    	return entries;
//    }
    
    @Override
    public void loadScripts() {
        getMapTile(21, 19).setInteractScript(new SimpleTextScript("Cat's house"));

        getMapTile(7, 26).setInteractScript(new SimpleTextScript("Walrus's house"));

        getMapTile(20, 4).setInteractScript(new SimpleTextScript("Dino's house"));

        getMapTile(2, 6).setInteractScript(new TreeScript());
        
        //added house entering scripts    
        getMapTile(17, 19).setInteractScript(new EnterHouseScript());
        
        getMapTile(4, 26).setInteractScript(new EnterWalrusHouseScript());
        
        getMapTile(17, 4).setInteractScript(new EnterDinoHouseScript());
    }

    @Override
    public MusicState getMusicState() {
        return MusicState.START;
    }
}