package ca.vanzeben.game.gfx;

public class Font {
	public static Font DEFAULT = new Font(
			new SpriteSheet("font", "/sprite_sheet.png", 32, 32));

	private static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      "
			+ "0123456789.,:;'\"!?$%()-=+/      ";

	private SpriteSheet fontSheet;
	
	public Font(SpriteSheet font) {
		this.fontSheet = font;
	}

	public Font(String path, int numTilesWidth, int numTilesHeight) {
		this.fontSheet = new SpriteSheet("font", path, numTilesHeight,
				numTilesWidth);
	}

	public void render(String msg, Screen screen, int x, int y, int scale) {
		msg = msg.toUpperCase();

		for (int i = 0; i < msg.length(); i++) {
			int charIndex = chars.indexOf(msg.charAt(i));
			if (charIndex >= 0)
				screen.render(x + (i * fontSheet.getSpriteWidth()*scale), y, fontSheet, charIndex + 30 * fontSheet.getNumSpritesWidth(),
						Screen.MirrorDirection.NONE, fontSheet.getSpriteWidth() * scale,
						fontSheet.getSpriteHeight() * scale);
		}
	}

	public int getWidthOf(String msg) {
		return msg.length()*fontSheet.getSpriteWidth();
	}
}