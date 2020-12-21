package ca.vanzeben.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ca.vanzeben.game.entities.ChasingEnemy;
import ca.vanzeben.game.entities.Chicken;
import ca.vanzeben.game.entities.Coin;
import ca.vanzeben.game.entities.Entity;
import ca.vanzeben.game.entities.Player;
import ca.vanzeben.game.entities.Tower;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static final int SCREEN_WIDTH = 240 * 4;
	private static final int SCREEN_HEIGHT = SCREEN_WIDTH / 12 * 9;
	private static final int SCALE = 1;

	private static final String NAME = "Game";
	private static final Dimension DIMENSIONS = new Dimension(SCREEN_WIDTH * SCALE,
			SCREEN_HEIGHT * SCALE);

	private Thread thread;
	private WindowHandler windowHandler;
	private static Game game;
	private static Screen screen;	
	private static Level level;
	private static InputHandler input;

	protected JFrame frame;

	private boolean running = false;
	private int tickCount = 0;

	private static Player player;

	protected boolean debug = false;
	protected boolean isApplet = false;

	public void init() {
		game = this;

		screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT);
		input = new InputHandler(this);
		level = new Level("/levels/water_test_level.png");
		
		level.setTileAt(3, 3, Tile.getStone());
		level.setTileAt(10, 2, Tile.getGrass2());
		
		//add Coins
		for (int i = 0; i < 100; i++) {
			int x = (int)(Math.random()*level.getLevelWidth());
			int y = (int)(Math.random()*level.getLevelHeight());
			Coin c = new Coin(x, y, SpriteSheet.tileSheet, 16, 12);
			level.addEntity(c);		
		}
		
		//add Chickens
		for (int i = 0; i < 50; i++) {
			int x = (int)(Math.random()*level.getLevelWidth());
			int y = (int)(Math.random()*level.getLevelHeight());
			Chicken e = new Chicken(x, y);
			level.addEntity(e);	
		}

		//add ChasingEnemy
		for (int i = 0; i < 30; i++) {
			int x = (int)(Math.random()*level.getLevelWidth());
			int y = (int)(Math.random()*level.getLevelHeight());
			ChasingEnemy e = new ChasingEnemy(x, y);
			level.addEntity(e);	
		}
		
		//add Towers
		for (int i = 0; i < 10; i++) {
			int x = (int)(Math.random()*level.getLevelWidth());
			int y = (int)(Math.random()*level.getLevelHeight());
			if (level.getTileTypeAtWorldCoordinates(x, y) != Tile.getWater()) {
				Tower t = new Tower(x, y);
				level.addEntity(t);
			} else {
				i++;
			}
		}
		
		player = new Player(level, 100, 100, input,
				JOptionPane.showInputDialog(this, "Please enter a username"),
				SpriteSheet.characterSheet);
		level.addPlayer(player);
		level.addEntity(player);
		
		
	}

	public static Player getPlayer() {
		return player;
	}

	protected void setPlayer(Player player) {
		this.player = player;
	}

	public synchronized void start() {
		running = true;

		thread = new Thread(this, getName() + "_main");
		thread.start();
	}

	public synchronized void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D; // Desired 60 frames per second

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double gameStepsToRun = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			gameStepsToRun += (now - lastTime) / nsPerTick;
			
			lastTime = now;
			boolean shouldRender = true;

			while (gameStepsToRun >= 1) {
				ticks++;
				tick();					//update everything
				gameStepsToRun -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();				//display everything
			}

			// if one second has passed, display frames per second (for informational purposes)
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				debug(DebugLevel.INFO, ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}

	/***
	 * Updates the game state once per frame.
	 */
	public void tick() {
		tickCount++;
		level.tick();
	}

	/***
	 * Render the game
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		screen.reset(); // You must call this BEFORE you render
										// anything!

		// Set screen position centered on player
		int screenX = player.getX() - (screen.getWidth() / 2);
		int screenY = player.getY() - (screen.getHeight() / 2);

		// Limit the screen position
		screenX = Math.max(0, screenX); // if < 0 set to 0
		screenY = Math.max(0, screenY);
		screenX = Math.min((level.getLevelWidth() - screen.getWidth()), screenX);
		screenY = Math.min((level.getLevelHeight() - screen.getHeight()), screenY);

		screen.setScreenPosition(screenX, screenY);

		// *****************************************************************************************
		// Do all rendering with screen here, after setting it graphics context
		//this is all of the drawing
		level.renderTiles(screen);
		level.renderEntities(screen);

		String msg = "Wizard Adventure";
		screen.renderTextAtScreenCoordinates(msg, Font.DEFAULT,
				screen.getWidth() - Font.DEFAULT.getWidthOf(msg) * 3, 10, 3);
		screen.renderTextAtScreenCoordinates("COIN COUNT: " + player.getNumCoins(), Font.DEFAULT, 0, 0, 2);
		screen.renderTextAtScreenCoordinates("HEALTH: " + player.getHealth(), Font.DEFAULT, 0, 25, 2);
		if (player.getHealth() <= 0) {
			screen.renderTextAtScreenCoordinates("GAME OVER", Font.DEFAULT, SCREEN_WIDTH / 3, SCREEN_HEIGHT / 3, 8);
		}

		if (debug) {
			screen.highlightTileAtScreenCoordinates(screen.getMouseX(),
					screen.getMouseY(), level.getTileDisplaySize());
			screen.displayMouseCoordinatesAtMouse();
			screen.displayPixelScale(50);
		}

		// *****************************************************************************************
		// Dispose of current context and show the rendered buffer

		g.drawImage(screen.getImage(), 0, 0, null);

		g.dispose();
		bs.show();
	}

	public static Screen getScreen() {
		return screen;
	}

	public void debug(DebugLevel level, String msg) {
		switch (level) {
		default:
		case INFO:
			if (debug) {
				System.out.println("[" + getName() + "] " + msg);
			}
			break;
		case WARNING:
			System.out.println("[" + getName() + "] [WARNING] " + msg);
			break;
		case SEVERE:
			System.out.println("[" + getName() + "] [SEVERE]" + msg);
			this.stop();
			break;
		}
	}
	
	public static void setTileType(Tile newTile, int x, int y) {
		level.setTileAt(x, y, newTile);
	}

	public static enum DebugLevel {
		INFO, WARNING, SEVERE;
	}

	public static Level getLevel() {
		return level;
	}

	public void setWindowHandler(WindowHandler wh) {
		this.windowHandler = wh;
	}

	public static Dimension getDimensions() {
		return DIMENSIONS;
	}

	public static String getName2() {
		return NAME;
	}
}
