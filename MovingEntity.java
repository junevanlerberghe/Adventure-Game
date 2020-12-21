package ca.vanzeben.game.entities;

import com.sun.xml.internal.bind.v2.runtime.Location;

import ca.vanzeben.game.gfx.SpriteSheet;

public abstract class MovingEntity extends Entity{

	protected double xSpeed;
	protected double ySpeed;
	protected double speed = 2;
	private int ticksToDirectionChange;
	private final int TIME_TO_CHANGE_DIR = 20;
	
	public MovingEntity(int x, int y, SpriteSheet sheet, int TILE_ROW, int TILE_COL) {
		super(x, y, sheet, TILE_ROW, TILE_COL);
		xSpeed = 0;
		ySpeed = 0;
	}
	
	public MovingEntity(int x, int y) {
		super(x, y);
	}
	
	public void moveRandomTowards(int posX, int posY) {
		
	}
	
	public void moveTowards(int posX, int posY) {
		double xComp = posX - x;
		double yComp = posY - y;
		double angle = Math.atan(yComp / xComp);
		
		if (xComp < 0)
			angle += Math.PI;
		
		this.xSpeed = speed*Math.cos(angle);
		this.ySpeed = speed*Math.sin(angle);	
	}
	
	public void movePlayerDirection(int x2, int y2) {
		if (ticksToDirectionChange <= 0) {
			moveTowards(x2, y2);
			ticksToDirectionChange = TIME_TO_CHANGE_DIR;
		}
		x += xSpeed;
		y += ySpeed;
		
		ticksToDirectionChange--;
	}
	
	public void chase(int x2, int y2) {
		movePlayerDirection(x2, y2);
	}
	
	public void move(int direction) {
		x += direction;
		y += direction;
	}
	
	public void setRandomDirection() {
		double randomAngle = Math.random()*2*Math.PI;
		double xComponent = Math.cos(randomAngle);
		double yComponent = Math.sin(randomAngle);
		
		this.xSpeed = speed*xComponent;
		this.ySpeed = speed*yComponent;
	}
	
	public void moveRandom() {
		if (ticksToDirectionChange <= 0) {
			setRandomDirection();
			ticksToDirectionChange = TIME_TO_CHANGE_DIR;
		}
		x += xSpeed;
		y += ySpeed;
		ticksToDirectionChange--;
	}
}
