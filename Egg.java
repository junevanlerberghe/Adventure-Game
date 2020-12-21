package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Egg extends Entity {
	
	private final static int DEFAULT_TILE_ROW = 24;
	private final static int DEFAULT_TILE_COL = 7;
	private final static SpriteSheet DEFAULT_SHEET = SpriteSheet.DungeonCrawl;
	private final static int TICKS_TO_OPEN = 10;
	private final static int TICKS_TO_DISSAPPEAR = TICKS_TO_OPEN + 5;
	
	private int ticksToOpen = TICKS_TO_OPEN;
	private int ticksToDissappear = TICKS_TO_DISSAPPEAR;
	private int chickenNumber = 5;

	public Egg(int x, int y) {
		super(x, y, DEFAULT_SHEET, DEFAULT_TILE_ROW, DEFAULT_TILE_COL);
	}
	
	public Egg(int x, int y, SpriteSheet sheet, int TILE_ROW, int TILE_COL) {
		super(x, y, sheet, TILE_ROW, TILE_COL);
	}

	protected int getTicksToOpen() {
		return ticksToOpen;
	}

	protected void setTicksToOpen(int ticksToOpen) {
		this.ticksToOpen = ticksToOpen;
	}

	public void tick() {
		if (ticksToDissappear <= 0) {
			Level.getEntities().remove(this);
		}
		if (ticksToOpen <= 0) {
			if (chickenNumber > 0) {
				Level.getEntities().add(new Chicken(x, y));	
				chickenNumber--;
			}
			
		}
		ticksToOpen--;
		ticksToDissappear--;
	}
	

}
