package ca.vanzeben.game.level.tiles;

import java.util.ArrayList;

/***
 * Represents a 2d location.
 * 
 * Used to represent locations of tiles in a SpriteSheet or player pixel coordinate location in the game.
 * 
 * @author David
 *
 */
public class Loc {
	private int x, y;

	public Loc(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static Loc asLoc(int x, int y) {
		return new Loc(x, y);
	}
	
	public static Loc asLoc(int[] coords) {
		return new Loc(coords[0], coords[1]);
	}
	
	/***
	 * Convert an array with rows as coordinate pairs into a Location list.
	 * @param coords
	 * @return
	 */
	public static ArrayList<Loc> asList(int[][] coords) {
		ArrayList<Loc> list = new ArrayList<Loc>();
		
		for (int level = 0; level < coords.length; level++) {
			list.add( Loc.asLoc(coords[level]) );
		}
		
		return list;
	}
	
	/***
	 * Return a List<Loc> with one element representing (x, y).  (Used in BasicTile.java constructors)
	 * @param x
	 * @param y
	 * @return
	 */
	public static ArrayList<Loc> asList(int x, int y) {
		ArrayList<Loc> list = new ArrayList<Loc>();
		list.add(Loc.asLoc(x, y));
		return list;
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
	
	public int getRow() {
		return y;
	}
	
	public int getCol() {
		return x;
	}
	
	public void setRow(int row) {
		y = row;
	}
	
	public void setCol(int col) {
		x = col;
	}
}