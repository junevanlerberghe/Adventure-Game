package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.gfx.SpriteSheet;

public class Tower extends Entity {
	
	private final static SpriteSheet DEFAULT_SHEET = SpriteSheet.DungeonCrawl;
	private final static int DEFAULT_TILE_ROW = 12, DEFAULT_TILE_COL = 13;
	
	private final int TICKS_TO_SHOOT = 200;
	private int ticksToShoot;
	private int fireSpeed;
	//private Entity target = Game.getPlayer();
	
	public Tower(int x, int y) {
		super(x, y, DEFAULT_SHEET, DEFAULT_TILE_ROW, DEFAULT_TILE_COL);
		this.fireSpeed = 2;
	}
	
	public Tower(int x, int y, SpriteSheet sheet, int TILE_ROW, int TILE_COL, Entity target) {
		super(x, y, sheet, TILE_ROW, TILE_COL);
	}
	
	
	public void shootFireball() {
		int x2 = Game.getPlayer().getX();
		int y2 = Game.getPlayer().getY();
		int xSpeed = (int) (fireSpeed * (int)(x2 - x) / Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y)));
		int ySpeed = (int) (fireSpeed * (int)(y2 - y) / Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y)));
		Game.getLevel().addEntity(new Fireball(this.x, this.y, xSpeed, ySpeed));
	}
	
	public void tick() {
		if (ticksToShoot <= 0) {
			shootFireball();
			ticksToShoot = TICKS_TO_SHOOT;
		}
		ticksToShoot--;
	}

}
