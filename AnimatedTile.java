package ca.vanzeben.game.level.tiles;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class AnimatedTile extends BasicTile {

	private int[][] animationTileCoords;
	private int currentAnimationIndex;
	private long lastIterationTime;
	private int animationSwitchDelay;
	
	//change animationcoords to a BasicTile[][] to make layered animation tiles
	public AnimatedTile(SpriteSheet sheet, int[][] animationCoords,
			int levelColour, int animationSwitchDelay) {
		super(sheet, animationCoords[0][0], animationCoords[0][1], levelColour);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}
	
	public void render(Screen screen, Level level, int x, int y, int displayWidth,
			int displayHeight) {
		screen.render(x, y, tileSheet, animationTileCoords[currentAnimationIndex][0],
				animationTileCoords[currentAnimationIndex][1], Screen.MirrorDirection.NONE, displayWidth, displayHeight);
	}

	public void tick() {
		if ((System.currentTimeMillis()
				- lastIterationTime) >= (animationSwitchDelay)) {
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1)
					% animationTileCoords.length;
		}
	}
}