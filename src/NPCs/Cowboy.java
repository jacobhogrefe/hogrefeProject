package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;
import java.util.HashMap;

// This class is for the walrus NPC
public class Cowboy extends NPC {
	
	

    public Cowboy(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Cowboy.png"), 42, 42), "STAND_LEFT");
        
    }

    public void update(Player player) {

        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(10, 17, 11, 7)
                            .withImageEffect(ImageEffect.FLIP_VERTICAL)
                            .build()
            });
            
            put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(10, 17, 11, 7)
                            .withImageEffect(ImageEffect.FLIP_VERTICAL)
                            .build()
            });

           
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
      
    }
    

    
  
}