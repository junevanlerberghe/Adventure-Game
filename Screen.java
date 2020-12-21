package ca.vanzeben.game.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.entities.Player;
import ca.vanzeben.game.gfx.Screen.MirrorDirection;
import ca.vanzeben.game.level.Level;

/***
 * Represents the graphics for the screen (what is actually displayed in the
 * window each frame). This does not represent the entire level which the screen
 * occupies a small part of.
 * 
 * There are two methods to use: setScreenPosition() sets the position of the
 * screen within the world coordinate system. render() is used to render
 * entities on the screen. This includes both background tiles and foreground
 * entities.
 * 
 * NOTE: run reset() before rendering anything if you wish to start from a blank
 * image. run getImage() to return the current image. The Game class does this
 * when it wants to paint the image on to the actual window pane.
 * 
 * @author David
 *
 */
public class Screen {
	private static final Color DEFAULT_COLOR = Color.BLACK;

	private boolean debug = false;

	public static enum MirrorDirection {
		Y, X, BOTH, NONE
	}

	private int x = 0; // Number of pixels to offset screen by (within the
											// level image)
	private int y = 0;

	private int width;
	private int height;

	private int mouseX, mouseY;

	private BufferedImage image;
	private Graphics graphicsContext = null;

	public Screen(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphicsContext = image.getGraphics();
		this.width = width;
		this.height = height;
	}

	public void reset() {
		graphicsContext.fillRect(0, 0, width, height);
	}

	/***
	 * Highlights the tile at world coordinates x, y
	 * 
	 * @param tx
	 * @param ty
	 */
	public void highlightTileAtWorldCoordinates(int tx, int ty, int tileSize) {
		if (graphicsContext == null) {
			System.err.println("Graphics context is null in highlightTile()");
			return;
		}

		int dx = (tx / tileSize) * tileSize;
		int dy = (ty / tileSize) * tileSize;

		dx -= x; // change world coordinates (xPos, yPos) to screen coordinates
		dy -= y;

		int destx1 = dx;
		int desty1 = dy;
		int destx2 = destx1 + tileSize;
		int desty2 = desty1 + tileSize;

		this.graphicsContext.setColor(Color.YELLOW);
		this.graphicsContext.drawRect(destx1, desty1, tileSize, tileSize);
		this.graphicsContext.setColor(DEFAULT_COLOR);
	}

	/***
	 * Render a tile on the screen at a particular location.
	 * 
	 * @param xPos
	 *          world x coordinate to render the tile
	 * @param yPos
	 *          world y coordinate to render the tile
	 * @param tileRow
	 *          row # in sprite sheet for the tile to be rendered
	 * @param tileCol
	 *          col # in the sprite sheet for the tile to be rendered
	 * @param colour
	 *          color
	 * @param mirrorDir
	 *          1 mirrors x, 2 mirrors y, 0 mirrors none, 3 mirrors both
	 * @param scale
	 */
	public void render(int xPos, int yPos, SpriteSheet sheet, int tileRow,
			int tileCol, MirrorDirection mirrorDir, int displayWidth,
			int displayHeight) {
		if (graphicsContext == null) {
			System.err.println(
					"Graphics context is null. Canvas object for Game must call setGraphicsContext( getBufferStrategy().getDrawGraphics() );");
		}
		xPos -= x; // change world coordinates (xPos, yPos) to screen
								// coordinates
		yPos -= y;

		int sourcex1 = tileCol * sheet.getSpriteWidth();
		int sourcey1 = tileRow * sheet.getSpriteHeight();

		int destx1 = xPos;
		int desty1 = yPos;

		BufferedImage tile = sheet.getImage().getSubimage(sourcex1, sourcey1,
				sheet.getSpriteWidth(), sheet.getSpriteHeight());

		double horizontalScale = displayWidth / (double) sheet.getSpriteWidth();
		double verticalScale = displayHeight / (double) sheet.getSpriteHeight();
		tile = processTile(tile, mirrorDir, horizontalScale, verticalScale);

		this.graphicsContext.drawImage(tile, destx1, desty1, null);

		// ****** DEBUG ******
		if (debug) {
			this.graphicsContext.drawRect(destx1, desty1, displayWidth,
					displayHeight);
		}
	}

	// Scale and mirror the tile before display
	private BufferedImage processTile(BufferedImage tile, MirrorDirection mirrorDir,
			double horizontalScale, double verticalScale) {
		
		if (mirrorDir == MirrorDirection.X) {
			horizontalScale *= -1;
			
			AffineTransform tx = AffineTransform.getScaleInstance(horizontalScale, verticalScale);
			tx.translate(-tile.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			
			return op.filter(tile, null);
		} else if (mirrorDir == MirrorDirection.Y) {
			verticalScale *= -1;
			
			AffineTransform tx = AffineTransform.getScaleInstance(horizontalScale, verticalScale);
			tx.translate(0, -tile.getHeight(null));
			AffineTransformOp op = new AffineTransformOp(tx,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			
			return op.filter(tile, null);
		} else if (mirrorDir == MirrorDirection.BOTH) {
			horizontalScale *= -1;
			verticalScale *= -1;
			
			AffineTransform tx = AffineTransform.getScaleInstance(horizontalScale, verticalScale);
			tx.translate(-tile.getWidth(null), -tile.getHeight(null));
			AffineTransformOp op = new AffineTransformOp(tx,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			return op.filter(tile, null);
		}
		
		AffineTransform tx = AffineTransform.getScaleInstance(horizontalScale, verticalScale);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		return op.filter(tile, null);
	}

	/***
	 * Render a tile to the screen at world coordinates xPos, yPos. This version
	 * does not re-scale the tile image. Call the overloaded render() to specify
	 * the final display size.
	 * 
	 * @param xPos
	 * @param yPos
	 * @param sheet
	 * @param tileRow
	 * @param tileCol
	 * @param mirrorDir
	 */
	public void render(int xPos, int yPos, SpriteSheet sheet, int tileRow,
			int tileCol, MirrorDirection mirrorDir) {

		render(xPos, yPos, sheet, tileRow, tileCol, mirrorDir,
				sheet.getSpriteWidth(), sheet.getSpriteHeight());
	}

	public void render(int xPos, int yPos, SpriteSheet sheet, int tileId,
			MirrorDirection mirrorDir, int displayWidth, int displayHeight) {
		render(xPos, yPos, sheet, tileId / sheet.getNumSpritesWidth(),
				tileId % sheet.getNumSpritesWidth(), mirrorDir, displayWidth,
				displayHeight);
	}

	public void renderTextAtWorldCoordinates(String msg, Font font, int x, int y,
			int scale) {
		font.render(msg, this, x, y, scale);
	}

	public void renderTextAtScreenCoordinates(String msg, Font font, int x, int y,
			int scale) {
		font.render(msg, this, screenXCoordToWorldCoord(x),
				screenYCoordToWorldCoord(y), scale);
	}

	/**
	 * Sets the screen position in the global (x, y) coordinate system.
	 * 
	 * @param xOffset
	 * @param yOffset
	 */
	public void setScreenPosition(int xOffset, int yOffset) {
		this.x = xOffset;
		this.y = yOffset;
	}

	public void setGraphicsContext(Graphics g) {
		this.graphicsContext = g;
	}

	/**
	 * Return world y-coordinate of upper left of screen.
	 * 
	 * @return
	 */
	public int getY() {
		return this.y;
	}

	public int getTopY() {
		return getY();
	}

	public int getBottomY() {
		return getY() + this.height;
	}

	/**
	 * Return world x-coordinate of upper left of screen.
	 * 
	 * @return
	 */
	public int getX() {
		return this.x;
	}

	public int getLeftX() {
		return getX();
	}

	public int getRightX() {
		return getX() + this.width;
	}

	public void displayPixelScale(int increment) {

		for (int dx = 0; dx < this.width; dx += increment) {
			int worldX = (x + dx);

			Font.DEFAULT.render("" + worldX, this, worldX, y + 10, 1);
		}

		for (int dy = 0; dy < this.height; dy += increment) {
			int worldY = (y + dy);

			Font.DEFAULT.render("" + worldY, this, x + 10, worldY, 1);
		}
	}

	public void displayMouseCoordinatesAtMouse() {
		this.graphicsContext.drawRect(mouseX, mouseY, 3, 3);
		Font.DEFAULT.render("" + mouseX + ", " + mouseY, this,
				screenXCoordToWorldCoord(mouseX), screenYCoordToWorldCoord(mouseY - 10),
				1);
	}

	public int worldXCoordToScreenCoord(int wx) {
		return wx - x;
	}

	public int screenXCoordToWorldCoord(int sx) {
		return (x + sx);
	}

	public int worldYCoordToScreenCoord(int wy) {
		return wy - y;
	}

	public int screenYCoordToWorldCoord(int sy) {
		return (y + sy);
	}

	public void highlightTileAtScreenCoordinates(int sx, int sy,
			int tileDisplaySize) {

		this.highlightTileAtWorldCoordinates(screenXCoordToWorldCoord(sx),
				screenYCoordToWorldCoord(sy), tileDisplaySize);
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseCoordinates(int x2, int y2) {
		mouseX = x2;
		mouseY = y2;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Image getImage() {
		return this.image;
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, Color c) {
		this.graphicsContext.setColor(c);
		this.graphicsContext.drawLine(x1, y1, x2, y2);
		this.graphicsContext.setColor(DEFAULT_COLOR);
	}
	
	public void drawRect(int x1, int y1, int width, int height, Color c) {
		this.graphicsContext.setColor(c);
		this.graphicsContext.drawRect(x1, y1, width, height);
		this.graphicsContext.setColor(DEFAULT_COLOR);
	}
	
	public void drawRoundRect(int x1, int y1, int width, int height, int arcWidth, int arcHeight, Color c) {
		this.graphicsContext.setColor(c);
		this.graphicsContext.drawRoundRect(x1, y1, width, height, arcWidth, arcHeight);
		this.graphicsContext.setColor(DEFAULT_COLOR);
	}

	public void drawOval(int x1, int y1, int width, int height, Color c) {
		this.graphicsContext.setColor(c);
		this.graphicsContext.drawOval(x1, y1, width, height);
		this.graphicsContext.setColor(DEFAULT_COLOR);
	}
}