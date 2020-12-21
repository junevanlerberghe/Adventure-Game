package ca.vanzeben.game.level.tiles;

import java.util.ArrayList;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class BasicTile extends Tile {

	/***
	 * Constructor for basic tile.
	 * 
	 * @param imageLocations
	 *          List of location objects representing row, col for each display
	 *          tile in the sprite sheet.
	 * @param levelColour
	 */
	public BasicTile(SpriteSheet sheet, ArrayList<Loc> imageLocations,
			int levelColour) {
		super(sheet, false, false, levelColour);
		this.imageLocLayers = imageLocations;
	}

	/***
	 * Constructor for basic tile.
	 * 
	 * @param imageLocations
	 *          array with each row a pair of coordinates for a tile image in the
	 *          tile SpriteSheet. Will be displayed row 0 up, so place
	 *          non-transparent tile at row 0, and any transparent overlays in
	 *          subsequent rows.
	 * 
	 * @param levelColour
	 */
	public BasicTile(SpriteSheet sheet, int[][] imageLocations, int levelColour) {
		this(sheet, Loc.asList(imageLocations), levelColour);
	}

	/***
	 * Constructor for basic tile
	 * 
	 * @param x
	 *          x coordinate for tile in sprite sheet. NOTE: not pixel coordinate.
	 *          If tile is the 3rd, tile over, x should be 2 (since tiles are
	 *          numbered starting at 0)
	 * @param y
	 *          y coordinate for tile in sprite sheet. (See note above)
	 * @param levelColour
	 *          color this tile will be represented by in level image.
	 */
	public BasicTile(SpriteSheet sheet, int x, int y, int levelColour) {
		this(sheet, Loc.asList(x, y), levelColour);
	}

	public void tick() {
	}

	/***
	 * World (x, y) coordinates.
	 */
	public void render(Screen screen, Level level, int x, int y, int displayWidth,
			int displayHeight) {
		for (int layer = 0; layer < this.imageLocLayers.size(); layer++) {
			screen.render(x, y, tileSheet, imageLocLayers.get(layer).getX(),
					imageLocLayers.get(layer).getY(), Screen.MirrorDirection.NONE,
					displayWidth, displayHeight);
		}
	}
}