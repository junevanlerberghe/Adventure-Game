package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Fireball extends MovingEntity {
	
	private final static int TICKS_TO_EXTINGUISH = 500;
	
	private int ticksToDie = TICKS_TO_EXTINGUISH;
	private Entity target;
	
	public Fireball(int x, int y, int xSpeed, int ySpeed) {
		super(x, y);
		this.TILE_ROW = 10;
		this.TILE_COL = 3;
		this.sheet = SpriteSheet.DungeonCrawl;
	}
	
	public Fireball(int x, int y, SpriteSheet sheet, int TILE_ROW, int TILE_COL) {
		super(x, y, sheet, TILE_ROW, TILE_COL);
	}
	
	public void handleCollision(Entity e2) {
		if (e2 instanceof Chicken || e2 instanceof ChasingEnemy)
			Level.removeEntity(e2);
	}

	public void tick() {
		if (ticksToDie > 0) {
			chase(Game.getPlayer().getX(), Game.getPlayer().getY());
			ticksToDie--;
		}
		else Level.removeEntity(this);
	}

}
