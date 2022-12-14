package Level;

import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Utils.Direction;

import java.util.HashMap;

// This class is a base class for all npcs in the game -- all npcs should extend from it
public class NPC extends MapEntity {
	protected int id = 0;

	protected boolean isWalrus = false;
	protected boolean isEatingGrass = false;
	
	//override methods in subclass
	protected boolean isTethered = false;
	protected boolean isTetherable = false;
	
	protected boolean isBlake = false;
	



	public boolean isTetherable() {
		return isTetherable;
	}

	public void setTetherable(boolean isTetherable) {
		this.isTetherable = isTetherable;
	}
	
	public boolean isBlake() {
		return isBlake;
	}

	public void setBlake(boolean isBlake) {
		this.isBlake = isBlake;
	}

	public NPC(int id, float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
		super(x, y, spriteSheet, startingAnimation);
		this.id = id;
	}

	public NPC(int id, float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
		super(x, y, animations, startingAnimation);
		this.id = id;
	}

	public NPC(int id, float x, float y, Frame[] frames) {
		super(x, y, frames);
		this.id = id;
	}

	public NPC(int id, float x, float y, Frame frame) {
		super(x, y, frame);
		this.id = id;
	}

	public NPC(int id, float x, float y) {
		super(x, y);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void facePlayer(Player player) {

		if (Math.round(getBoundsX2()) - (getBounds().getWidth() / 2) < Math.round(player.getBoundsX2())) {
			this.currentAnimationName = "STAND_RIGHT";
		} else if (Math.round(getBoundsX1()) + (getBounds().getWidth() / 2) > Math.round(player.getBoundsX1())) {
			this.currentAnimationName = "STAND_LEFT";
		}
	}

	public void stand(Direction direction) {
		if (direction == Direction.RIGHT) {
			this.currentAnimationName = "STAND_RIGHT";
		} else if (direction == Direction.LEFT) {
			this.currentAnimationName = "STAND_LEFT";
		}
	}

	public void walk(Direction direction, float speed) {
		if (direction == Direction.RIGHT) {
			this.currentAnimationName = "WALK_RIGHT";
		} else if (direction == Direction.LEFT) {
			this.currentAnimationName = "WALK_LEFT";
		} else {
			if (this.currentAnimationName.contains("RIGHT")) {
				this.currentAnimationName = "WALK_RIGHT";
			} else {
				this.currentAnimationName = "WALK_LEFT";
			}
		}

		if (direction == Direction.UP) {
			moveY(-speed);
		} else if (direction == Direction.DOWN) {
			moveY(speed);
		} else if (direction == Direction.LEFT) {
			moveX(-speed);
		} else if (direction == Direction.RIGHT) {
			moveX(speed);
		}
	}
	
	public void eatGrass() {
		if(isTetherable() || isBlake()) {
			this.currentAnimationName = "EAT_GRASS";
			this.isEatingGrass = true;
		}
		else {
			
		}
	}
	
	public void becomeBlake() {
		if(isBlake()) {
			this.currentAnimationName = "BLAKE";
			this.isEatingGrass = false;
		}
		else {
			
		}
	}

	public void setEatGrass(boolean isEatingGrass) {
		this.isEatingGrass = isEatingGrass;
	}

	public boolean isEatingGrass() {
		return this.isEatingGrass;
	}
	
	
	//override in subclass
	public boolean isTethered() {
		return isTethered;
	}

	//override in subclass
	public void setTether(boolean isTethered, Player player) {

	}

	public void update(Player player) {
		super.update();
	}

	@Override
	public void draw(GraphicsHandler graphicsHandler) {
		super.draw(graphicsHandler);
	}

	
}
