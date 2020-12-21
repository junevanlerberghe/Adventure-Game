package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.InputHandler;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.BasicSolidTile;
import ca.vanzeben.game.level.tiles.Tile;

public class Player extends MovingEntity {
	private boolean debug = false;
	private int x, y;
	private int width, height;
	private Level level;

	protected String name;
	protected int speed = 3;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;

	private InputHandler input;
	protected boolean isSwimming = false;
	private int tickCount = 0;
	private String username;
	private int SFCoinCount = 50;

	private SpriteSheet sheet;
	
	private int numCoins = 0;
	private int health = 50;

	public Player(Level level, int x, int y, InputHandler input, String username,
			SpriteSheet sheet) {
		super(x, y);
		this.level = level;
		this.input = input;
		this.username = username;
		this.sheet = sheet;
		this.width = sheet.getSpriteWidth();
		this.height = sheet.getSpriteHeight();
	}

	/***
	 * Move the player by dx, dy
	 * 
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if (dx != 0 && dy != 0) {
			move(dx, 0);
			move(0, dy);
			numSteps--;
			return;
		}

		numSteps++;
		if (!willCollidedWithTile(dx, dy)) {
			if (dy < 0)
				movingDir = 0;
			if (dy > 0)
				movingDir = 1;
			if (dx < 0)
				movingDir = 2;
			if (dx > 0)
				movingDir = 3;
			x += dx;
			y += dy;
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int newNum) {
		health = newNum;
	}
	
	public void handleCollision(Entity e2) {
		if (e2 instanceof Coin) {
			if (isCollidingWith(e2)) {
				Level.removeEntity(e2);
				setNumCoins(getNumCoins() + 1);
			}
		} 
		if (e2 instanceof ChasingEnemy || e2 instanceof Fireball) {
			if (isCollidingWith(e2)) {
				Level.removeEntity(e2);
				setHealth(getHealth() - 1);
			}
		}
	}

	public Tile getCurrentTileType() {
		return this.level.getTileTypeAtWorldCoordinates(x, y);
	}

	public int getX() {
		return x;
	}

	public int leftX() {
		return x;
	}

	public int rightX() {
		return leftX() + this.width;
	}

	public int getY() {
		return y;
	}

	public int topY() {
		return y;
	}

	public int bottomY() {
		return topY() + this.height;
	}

	public int centerX() {
		return x + this.width / 2;
	}

	public int centerY() {
		return y + this.height / 2;
	}

	public String getName() {
		return name;
	}

	public int getNumSteps() {
		return numSteps;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int getMovingDir() {
		return movingDir;
	}

	public void tick() {
		int xDir = 0;
		int yDir = 0;
		if (input != null) {
			if (input.up.isPressed()) {
				yDir--;
				
			}
			if (input.down.isPressed()) {
				yDir++;
			}
			if (input.left.isPressed()) {
				xDir--;
			}
			if (input.right.isPressed()) {
				xDir++;
			}
		}

		if (xDir != 0 || yDir != 0) {
			move(xDir * speed, yDir * speed);
			isMoving = true;
		} else {
			isMoving = false;
		}

		tickCount++;
	}
	
	public boolean contains(int x, int y) {
		if (x > getX() && x < getX() + width && y > getY() && y < getY() + height) 
			return true;
		return false;
	}
	
	public boolean isCollidingWith(Entity c) {
		if (contains(c.getX(), c.getY())) 
			return true;
		return false;
	}
	
	public void render(Screen screen) {
		if (input != null && input.t.isPressed()) {
			screen.render(x, y, SpriteSheet.tileSheet, 11, 0, Screen.MirrorDirection.NONE);
		} else if (level.getTileTypeAtWorldCoordinates(x + width/2, y + height/2) == Tile.getWater()){
			screen.render(x, y, SpriteSheet.DungeonCrawl, 45, 37, Screen.MirrorDirection.NONE);
		} else if (input.up.isPressed()){
			screen.render(x, y, sheet, 5, 0, Screen.MirrorDirection.NONE);
		} else if (input.left.isPressed()) {
			screen.render(x, y, sheet, 0, 7, Screen.MirrorDirection.NONE);
		} else if (input.right.isPressed()) {
			screen.render(x, y, sheet, 0, 7, Screen.MirrorDirection.X);
		} else if (input != null && input.e.isPressed()) {
			Level.getEntities().add(new Egg(x, y));
		} else if (input != null && input.coin.isPressed()) {
			SFCoinCount--;
			if (SFCoinCount >= 0) {
				setNumCoins(getNumCoins() + 1);
				Game.getScreen().renderTextAtScreenCoordinates("special feature found, more coins added!!", Font.DEFAULT,
					Game.getScreen().worldXCoordToScreenCoord(x), Game.getScreen().worldYCoordToScreenCoord(y), 1);
			} else Game.getScreen().renderTextAtScreenCoordinates("feature maximum use reached :(", Font.DEFAULT,
					Game.getScreen().worldXCoordToScreenCoord(x), Game.getScreen().worldYCoordToScreenCoord(y), 1);
		} else if (input != null && input.space.isPressed()) {
			Level.getEntities().add(new Fireball2(x, y));
		} else {
			renderAnimatedStanding(screen);
		}
		if (debug) {
			renderDebuggingElements(screen);
		}

		if (username != null) {
			screen.renderTextAtWorldCoordinates(username, Font.DEFAULT, centerX() - Font.DEFAULT.getWidthOf(username)/2, y - 10, 1);
		}
	}

	public int getSpeed() {
		return speed;
	}
	private void renderAnimatedStanding(Screen screen) {
		if (tickCount % 60 < 15) {		
			screen.render(x, y, sheet, 0, 0, Screen.MirrorDirection.NONE);
		} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
			screen.render(x, y, sheet, 0, 1, Screen.MirrorDirection.NONE);
		} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
			screen.render(x, y, sheet, 0, 2, Screen.MirrorDirection.NONE);
		} else {
			screen.render(x, y, sheet, 0, 3, Screen.MirrorDirection.NONE);
		}
	}
	

	/***
	 * Check if player is going to collide with a solid tile if x changes by dx
	 * and y changes by dy
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	public boolean willCollidedWithTile(int dx, int dy) {
		// Calculate coordinates of all 4 corners of player sprite
		// Check each for collision

		Tile upperLeftTile = level.getTileTypeAtWorldCoordinates(leftX() + dx,
				topY() + dy);
		Tile lowerLeftTile = level.getTileTypeAtWorldCoordinates(leftX() + dx,
				bottomY() + dy);
		Tile upperRightTile = level.getTileTypeAtWorldCoordinates(rightX() + dx,
				topY() + dy);
		Tile lowerRightTile = level.getTileTypeAtWorldCoordinates(rightX() + dx,
				bottomY() + dy);

		if (upperLeftTile.isSolid() || lowerLeftTile.isSolid()
				|| upperRightTile.isSolid() || lowerRightTile.isSolid())
			return true;
		return false;
	}

	public String getUsername() {
		return this.username;
	}

	private void renderDebuggingElements(Screen screen) {
		screen.highlightTileAtWorldCoordinates(leftX(), topY(),
				level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(leftX(), bottomY(),
				level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), topY(),
				level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), bottomY(),
				level.getTileDisplaySize());

		Font.DEFAULT.render("" + x + ", " + y, screen,
				x - ((username.length() - 1) / 2 * 8), y - 10, 1);
	}

	public int getNumCoins() {
		return numCoins;
	}

	public void setNumCoins(int numCoins) {
		this.numCoins = numCoins;
	}
}