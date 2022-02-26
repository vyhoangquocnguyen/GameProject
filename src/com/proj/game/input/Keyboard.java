package com.proj.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{
	
	private boolean[] keys = new boolean[120]; //65536 is the maximum
	public boolean up, down, left, right;
	
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		
	}
	
	public void keyTyped(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
	}

	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

	
	public void keyReleased(KeyEvent e) {

		
	}
	

}
