package com.proj.game.graphics;

import java.util.Random;

public class Screen {

	private int width, height;
	public int[] pixels;
	public final int MAP_SIZE = 64; //seting the MAP SIZE for the tile
	public final int MAP_SIZE_MASK = MAP_SIZE -1;
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE]; //MAP_SIZE * MAP_SIZE = tile.length();
	private Random random = new Random(); //random number generator

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];

		for (int i = 0; i < MAP_SIZE * MAP_SIZE; i++) {
			// generate random color # and put in into the tile[i[
			tiles[i] = random.nextInt(0xffffff); 
		}

	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void render() {
		for (int y = 0; y < height; y++) {
			int yy = y;
			if (yy < 0 || yy >= height) break;
			for (int x = 0; x < width; x++) {
				int xx = x;
				if (xx < 0 || xx >= width) break;
				//first 16 pixels of x or y has to have the same color
				int tileIndex = ((xx >> 4)& MAP_SIZE_MASK) + ((yy >> 4) & MAP_SIZE_MASK) * MAP_SIZE; // >>4 = /16 (2^4)
				/*System.out.println(tileIndex); tileIndex repeats 16 times 
				Hence the 16 x 16 pixels has the same color of tiles*/
				//pixel length = 48600 / 16 / 16 = Color blocks(one tiles) on screen
				pixels[x + y * width] = tiles[tileIndex];
			}
			
		}
	}
}
