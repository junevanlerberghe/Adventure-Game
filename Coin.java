package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.Screen.MirrorDirection;
import ca.vanzeben.game.gfx.SpriteSheet;

public class Coin extends Entity {
	
	public Coin(int x, int y) {
		super(x, y);
		sheet = SpriteSheet.tileSheet;
		TILE_ROW = 16;
		TILE_COL = 12;
	}
	
	public Coin(int x, int y, SpriteSheet sheet, int xPos, int yPos) {
		super(x, y);
		this.sheet = sheet;
		TILE_ROW = xPos;
		TILE_COL = yPos;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void render(Screen screen) {
		screen.render(x, y, sheet, TILE_ROW, TILE_COL, MirrorDirection.NONE);
	}

	public void tick() {
		//blank because coin does not move	
	}
}
