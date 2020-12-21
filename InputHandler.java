package ca.vanzeben.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ca.vanzeben.game.gfx.Screen;

public class InputHandler implements KeyListener, MouseMotionListener {

	public InputHandler(Game game) {
		game.addKeyListener(this);
		game.addMouseMotionListener(this);
	}

	public class Key {
		private int numTimesPressed = 0;
		private boolean pressed = false;

		public int getNumTimesPressed() {
			return numTimesPressed;
		}

		public boolean isPressed() {
			return pressed;
		}

		public void toggle(boolean isPressed) {
			pressed = isPressed;
			if (isPressed)
				numTimesPressed++;
		}
	}

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key t = new Key();
	public Key e = new Key();
	public Key coin = new Key();
	public Key space = new Key();

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
			up.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
			down.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
			left.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
			right.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_T) {
			t.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_E) {
			e.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_2) {
			coin.toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			space.toggle(isPressed);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();

		Game.getScreen().setMouseCoordinates(x, y);
	}
}
