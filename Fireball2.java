package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.gfx.SpriteSheet;

public class Fireball2 extends MovingEntity{

	private static final SpriteSheet DEFAULT_SHEET = SpriteSheet.DungeonCrawl;
	private static final int DEFAULT_TILE_ROW = 10;
	private static final int DEFAULT_TILE_COL = 0;
	
	public Fireball2(int x, int y) {
		super(x, y, DEFAULT_SHEET, DEFAULT_TILE_ROW, DEFAULT_TILE_COL);	
	}

	public void tick() {
		int direction = Game.getPlayer().getSpeed() * (int) Math.atan2(Game.getPlayer().getX(), Game.getPlayer().getY());
		move(direction);
	}

}
