package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.Screen.MirrorDirection;
import ca.vanzeben.game.gfx.SpriteSheet;

public class Chicken extends MovingEntity{
	
	private final static int DEFAULT_TILE_ROW = 16;
	private final static int DEFAULT_TILE_COL = 7;
	private final static SpriteSheet DEFAULT_SHEET = SpriteSheet.tileSheet;
	
	public Chicken(int x, int y, SpriteSheet sheet, int TILE_ROW, int TILE_COL) {
		super(x, y, sheet, TILE_ROW, TILE_COL);
	}
	
	public Chicken(int x, int y) {
		super(x, y, DEFAULT_SHEET, DEFAULT_TILE_ROW, DEFAULT_TILE_COL);
	}
	
	public void render(Screen screen) {
		screen.render(x, y, sheet, TILE_COL, TILE_ROW, MirrorDirection.NONE);
	}
	
	public void tick() {
		moveRandom();
	}

}
