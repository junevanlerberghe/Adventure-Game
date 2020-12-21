package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.Screen.MirrorDirection;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public abstract class Entity {

	protected int x, y, spriteX, spriteY, height, width;
	protected int TILE_COL, TILE_ROW;
	protected SpriteSheet sheet;
	protected boolean isDead;
	
	public abstract void tick();
	
	public Entity(int x, int y, SpriteSheet sheet, int TILE_ROW, int TILE_COL) {
		this.x = x;
		this.y = y;
		this.TILE_COL = TILE_COL;
		this.TILE_ROW = TILE_ROW;
		this.sheet = sheet;
		this.height = sheet.getSpriteHeight();
		this.width = sheet.getSpriteWidth();
	}
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void render(Screen screen) {
		screen.render(x, y, sheet, TILE_ROW, TILE_COL, MirrorDirection.NONE);
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
	public SpriteSheet getSheet() {
		return sheet;
	}
	public void setSheet(SpriteSheet sheet) {
		this.sheet = sheet;
	}
	public int getSpriteX() {
		return spriteX;
	}
	public void setSpriteX(int spriteX) {
		this.spriteX = spriteX;
	}
	public int getSpriteY() {
		return spriteY;
	}
	public void setSpriteY(int spriteY) {
		this.spriteY = spriteY;
	}
	public void handleCollision(Entity e2) {
		//by default, no collisions are handled
		//override this method in children
	}
	public boolean contains(int x, int y) {
		if (x > this.x && x < this.x + width && y > this.y && y < this.y + height) 
			return true;
		return false;
	}
	
	public boolean isCollidingWith(Entity e2) {
		if (contains(e2.getX(), e2.getY())) 
			return true;
		return false;
	}
	
	protected int getHeight() {
		return height;
	}
	protected void setHeight(int height) {
		this.height = height;
	}
	protected int getWidth() {
		return width;
	}
	protected void setWidth(int width) {
		this.width = width;
	}
	protected boolean isDead() {
		return isDead;
	}
	protected void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
}
