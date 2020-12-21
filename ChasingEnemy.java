package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.Screen.MirrorDirection;
import ca.vanzeben.game.gfx.SpriteSheet;

public class ChasingEnemy extends MovingEntity{
	
	private Entity target = Game.getPlayer();
	
	private final static int DEFAULT_TILE_ROW = 3;
	private final static int DEFAULT_TILE_COL = 23;
	private final static SpriteSheet DEFAULT_SHEET = SpriteSheet.DungeonCrawl;
	
	
	public ChasingEnemy(int x, int y, SpriteSheet sheet, int TILE_ROW, int TILE_COL) {
		super(x, y, sheet, TILE_ROW, TILE_COL);
	}
	
	public ChasingEnemy(int x, int y) {
		super(x, y, DEFAULT_SHEET, DEFAULT_TILE_ROW, DEFAULT_TILE_COL);
	}

	public void render(Screen screen) {
		screen.render(x, y, sheet, TILE_ROW, TILE_COL, MirrorDirection.NONE);
	}

	public void tick() {
		chase(Game.getPlayer().getX(), Game.getPlayer().getY());
	}

}
