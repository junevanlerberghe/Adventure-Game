package ca.vanzeben.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.entities.ChasingEnemy;
import ca.vanzeben.game.entities.Chicken;
import ca.vanzeben.game.entities.Coin;
import ca.vanzeben.game.entities.Egg;
import ca.vanzeben.game.entities.Entity;
import ca.vanzeben.game.entities.Fireball;
import ca.vanzeben.game.entities.MovingEntity;
import ca.vanzeben.game.entities.Player;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.tiles.Tile;

public class Level {
	private static final int originalTileSize = 30;		// from sprite sheet
	private static final int scaleFactor = 2;         // how much to scale the tiles up for display
	
	private static final int tileSize = originalTileSize*scaleFactor; // each pixel in game ends up
																								 									 // being this large
	private static ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private int[][] levelTileIds;
	private int levelImageWidth;
	private int levelImageHeight;
	private String imagePath;
	private BufferedImage levelSourceimage;

	private Player player;

	public Level(String imagePath) {
		if (imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else {
			this.levelImageWidth = 64;
			this.levelImageHeight = 64;
			levelTileIds = new int[levelImageHeight][levelImageWidth];
			this.generateLevel();
		}

	}

	private void loadLevelFromFile() {
		try {
			this.levelSourceimage = ImageIO.read(Level.class.getResource(this.imagePath));
			this.levelImageWidth = this.levelSourceimage.getWidth();
			this.levelImageHeight = this.levelSourceimage.getHeight();
			levelTileIds = new int[levelImageHeight][levelImageWidth];
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTiles() {
		int[] tileColours = this.levelSourceimage.getRGB(0, 0, levelImageWidth,
				levelImageHeight, null, 0, levelImageWidth);
		for (int y = 0; y < levelImageHeight; y++) {
			for (int x = 0; x < levelImageWidth; x++) {
				tileCheck: for (Tile t : Tile.getTiles()) {
					if (t != null
							&& t.getLevelColour() == tileColours[x + y * levelImageWidth]) {
						this.levelTileIds[y][x] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void saveLevelToFile() {
		try {
			ImageIO.write(levelSourceimage, "png",
					new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTileAt(int x, int y, Tile newTile) {
		this.levelTileIds[y][x] = newTile.getId();
		levelSourceimage.setRGB(x, y, newTile.getLevelColour());
	}

	public void generateLevel() {
		for (int y = 0; y < levelImageHeight; y++) {
			for (int x = 0; x < levelImageWidth; x++) {
				if (x * y % 10 < 7) {
					levelTileIds[y][x] = Tile.getGrass().getId();
				} else {
					levelTileIds[y][x] = Tile.getStone().getId();
				}
			}
		}
	}
	
	public boolean isEntityAt(int xPos, int yPos) {
		for (int i = 0; i < getEntities().size(); i++) {
			if (getEntities().get(i).getX() == xPos && getEntities().get(i).getY() == yPos)
				return true;
		}
		return false;
	}

	/***
	 * Run tick() on everything in this level to prepare for next game frame.
	 */
	public void tick() {
		// Run tick() for all entities
		player.tick();

		for (int i = 0; i < getEntities().size(); i++) {
			Entity e = getEntities().get(i);
			e.tick();
		}
		
		// Run tick() for all tiles
		for (Tile t : Tile.getTiles()) {
			if (t == null) {
				break;
			}
			t.tick();
		}	
		//COLLISION DETECTION
		
		for (int i = 0; i < getEntities().size(); i++) {
			Entity e1 = getEntities().get(i);
			for(int j = 1; j < getEntities().size(); j++) {
				Entity e2 = getEntities().get(j);
				if (e1.isCollidingWith(e2)) {
					e1.handleCollision(e2);
					e2.handleCollision(e1);
				}
			}
		}
		
	}	

	public void renderTiles(Screen screen) {
		for (int tileY = screen.getTopY()
				/ tileSize; tileY < screen.getBottomY() / tileSize + 1; tileY++) {
			for (int tileX = (screen.getLeftX() / tileSize); tileX < screen.getRightX() / tileSize + 1; tileX++) {
				getTileAtSourceImageCoordinates(tileX, tileY).render(screen, this, tileX * tileSize,
						tileY * tileSize, tileSize, tileSize);
			}
		}
	}

	public void renderEntities(Screen screen) {
		player.render(screen);

		for (int i = 0; i < getEntities().size(); i++) {
			Entity e = getEntities().get(i);
			e.render(screen);
		}
	}

	public Tile getTileTypeAtWorldCoordinates(int x, int y) {
		if (0 > x || x >= this.getLevelWidth() || 0 > y || y >= this.getLevelHeight())
			return Tile.getVoid();
		
		int sourcex = x / this.tileSize;
		int sourcey = y / this.tileSize;
		
		//Game.getScreen().highlightTileAtWorldCoordinates(x, y, tileSize);
		
		int tileId = levelTileIds[sourcey][sourcex];
		return Tile.getTiles().get(tileId);
	}
	
	public Tile getTileAtSourceImageCoordinates(int x, int y) {
		if (0 > x || x >= levelImageWidth || 0 > y || y >= levelImageHeight)
			return Tile.getVoid();
		
		int tileId = levelTileIds[y][x];
		return Tile.getTiles().get(tileId);
	}

	public void addPlayer(Player player) {
		this.player = player;
	}
	
	public static ArrayList<Entity> getEntities() {
		return entities;
	}
 	
	public void addEntity(Entity e) {
		getEntities().add(e);
	}
	
	public static void removeEntity(Entity e) {
		getEntities().remove(e);
	}

	/***
	 * Return size of level image. NOTE: this is NOT the width of the level
	 * itself. Only the level's source image.
	 * 
	 * @return
	 */
	public int getLevelImageWidth() {
		return levelImageWidth;
	}

	/***
	 * Return size of level image. NOTE: this is NOT the height of the level
	 * itself. Only the level's source image.
	 * 
	 * @return
	 */
	public int getLevelImageHeight() {
		return levelImageHeight;
	}

	/***
	 * Return pixel width of the level in world coordinates. Note: this is
	 * different from the size of the level *image* which is scaled up by a factor
	 * of levelScaleFactor to create the level
	 * 
	 * @return
	 */
	public int getLevelWidth() {
		return getLevelImageWidth() * this.tileSize;
	}

	/***
	 * Return pixel height of the level in world coordinates. Note: this is
	 * different from the size of the level *image* which is scaled up by a factor
	 * of levelScaleFactor to create the level
	 * 
	 * @return
	 */
	public int getLevelHeight() {
		return getLevelImageHeight() * this.tileSize;
	}

	public int getTileDisplaySize() {
		return this.tileSize;
	}
}
