package com.proj.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.proj.game.graphics.Screen;
import com.proj.game.input.Keyboard;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	public static String title = "GameP";

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private boolean running = false;

	private Screen screen;

	/*Basically converting the image object into an array of integer
	 * then the array of the integer will signal which pixel receive
	 * which color  */

	// Create an image
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	// Allow to draw things on that image
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	/****/

	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		frame = new JFrame();
		screen = new Screen(width, height);
		key = new Keyboard();
		
		addKeyListener(key);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
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

	// Runnable will start run() automatically
	public void run() {
		/*Timer*/
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1e9 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus(); //focus window
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();// handle all the logic
				updates++;
				delta--;
			}
			render();// display the images of the game
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer+=1000;
				frame.setTitle(title + "  |  " + updates + " ups " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	/******/
	int x = 0, y=0;
	/*****/
	public void update() {
		key.update();
		if (key.up) y--;
		if (key.down) y++;
		if (key.left) x--;
		if (key.right) x++;
		
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			// create triple buffers
			createBufferStrategy(3);
			return;
		}
		// clear screen
		screen.clear();
		// then render
		screen.render(x,y);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		//System.out.println(pixels.length); // Check the lenghth of pixels

		// getting a link between graphic with buffer
		Graphics g = bs.getDrawGraphics();
		/*		// set color
				g.setColor(Color.BLACK); // g.setColor(new Color(R,G,B))
				// fill the rectangle
				g.fillRect(0, 0, getWidth(), getHeight());*/
		// Draw the buffer to the screen
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		// release all the system resources after render a frame.
		g.dispose();
		// swapping the buffers
		// show the buffer being calculated( make the next buffer available)
		bs.show();

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false); // important
		game.frame.setTitle(Game.title);
		// addable because Game is inheritated from Canvas
		game.frame.add(game);
		// set the frame to the same size of the component
		game.frame.pack();
		// Terminate the application when close
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Centering the window
		game.frame.setLocationRelativeTo(null);
		// Show the frame
		game.frame.setVisible(true);

		// start the game
		game.start();

	}
}
