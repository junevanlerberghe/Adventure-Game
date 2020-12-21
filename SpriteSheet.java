package ca.vanzeben.game.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	public static final SpriteSheet DungeonCrawl = new SpriteSheet("dungeon",
			"/DungeonCrawl_ProjectUtumnoTileset.png", 48, 64);

	public static final SpriteSheet characterSheet = new SpriteSheet("characters",
			"/entities/wizard.png", 8, 8);

	public static final SpriteSheet tileSheet = new SpriteSheet("tiles",
			"/tilesheet.png", 19, 20);

	private String name;
	private String path;
	private int pixelWidth;
	private int pixelHeight;
	private int numSpritesWidth;
	private int numSpritesHeight;

	private int spriteWidth, spriteHeight;

	private int[] pixels;
	private BufferedImage sheetImage;

	public SpriteSheet(String name, String path, int numSpritesHeight,
			int numSpritesWidth) {
		sheetImage = null;
		try {
			sheetImage = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (sheetImage == null) {
			return;
		}

		this.name = name;
		this.path = path;
		this.pixelWidth = sheetImage.getWidth();
		this.pixelHeight = sheetImage.getHeight();
		this.numSpritesHeight = numSpritesHeight;
		this.numSpritesWidth = numSpritesWidth;

		this.spriteWidth = this.pixelWidth / this.numSpritesWidth;
		this.spriteHeight = this.pixelHeight / this.numSpritesHeight;

		pixels = sheetImage.getRGB(0, 0, pixelWidth, pixelHeight, null, 0,
				pixelWidth);
	}

	/***
	 * Returns a unique id in the 2d sprite array for each sprite. Note that row,
	 * col are NOT pixel coordinates. They are the coordinates for which sprite in
	 * the grid is desired. Upper left sprite is 0, 0. Unique id's are assigned in
	 * left-to-right top-to-bottom order.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public int idFor(int row, int col) {
		return row * numSpritesWidth + col;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public void setPixelWidth(int pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public void setPixelHeight(int pixelHeight) {
		this.pixelHeight = pixelHeight;
	}

	public int getNumSpritesWidth() {
		return numSpritesWidth;
	}

	public void setNumSpritesWidth(int numSpritesWidth) {
		this.numSpritesWidth = numSpritesWidth;
	}

	public int getNumSpritesHeight() {
		return numSpritesHeight;
	}

	public void setNumSpritesHeight(int numSpritesHeight) {
		this.numSpritesHeight = numSpritesHeight;
	}

	public int[] getPixels() {
		return pixels;
	}

	public int getSpriteHeight() {
		return this.spriteHeight;
	}

	public int getSpriteWidth() {
		return this.spriteWidth;
	}

	public void displayInfo() {
		System.out.println("Spritesheet: " + name);
		System.out.println("Full spritesheet pixel size: " + this.pixelWidth + ", "
				+ this.pixelHeight);
		System.out.println("Num sprites (rows, cols): " + this.numSpritesWidth
				+ ", " + this.numSpritesHeight);
		System.out.println(
				"Size per sprite: " + this.spriteWidth + ", " + this.spriteHeight);
	}

	public BufferedImage getImage() {
		return this.sheetImage;
	}
}
