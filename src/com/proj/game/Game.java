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

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;

	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	
	private Screen screen;
	
	/*Basically converting the image object into an array of integer
	 * then the array of the integer will signal which pixel receive
	 * which color  */
	
	//Create an image
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	//Allow to draw things on that image
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	/****/
	
	public Game() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);

		frame = new JFrame();
		screen = new Screen(width, height);
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

		while (running) {
			update();// handle all the logic
			render();// display the images of the game
		}
	}

	public void update() {

	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			//create triple buffers
			createBufferStrategy(3);
			return;
		}
		//clear screen
		screen.clear();
		//then render
		screen.render();
		
		for(int i =0 ; i< pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		// getting a link between graphic with buffer
		Graphics g = bs.getDrawGraphics();
		/*		// set color
				g.setColor(Color.BLACK); // g.setColor(new Color(R,G,B))
				// fill the rectangle
				g.fillRect(0, 0, getWidth(), getHeight());*/
		//Draw the buffer to the screen
		g.drawImage(image,0, 0, getWidth(), getHeight(),null);
		// release all the system resources after render a frame.
		g.dispose();
		// swapping the buffers
		// show the buffer being calculated( make the next buffer available)
		bs.show();

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false); // important
		game.frame.setTitle("GameN");
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
