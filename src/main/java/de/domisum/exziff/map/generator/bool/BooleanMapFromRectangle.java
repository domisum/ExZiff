package de.domisum.exziff.map.generator.bool;

import de.domisum.exziff.map.BooleanMap;

public class BooleanMapFromRectangle extends BooleanMapGenerator
{

	// SETTINGS
	private int width;
	private int height;

	private double minX;
	private double maxX;
	private double minY;
	private double maxY;


	// INITIALIZATION
	public BooleanMapFromRectangle(int width, int height, double x1, double y1, double x2, double y2)
	{
		this.width = width;
		this.height = height;

		if(x1 < 0 || x1 > 1 || y1 < 0 || y1 > 1 || x2 < 0 || x2 > 1 || y2 < 0 || y2 > 1)
			throw new IllegalArgumentException("All coordinates have to be between 0.0 and 1.0");

		this.minX = Math.min(x1, x2);
		this.maxX = Math.max(x1, x2);
		this.minY = Math.min(y1, y2);
		this.maxY = Math.max(y1, y2);
	}


	// GENERATION
	@Override public BooleanMap generate()
	{
		boolean[][] pixels = new boolean[this.height][this.width];

		for(int y = 0; y < this.height; y++)
			for(int x = 0; x < this.width; x++)
			{
				double rX = x/(double) this.width;
				double rY = y/(double) this.height;

				if(this.minX <= rX && rX <= this.maxX && this.minY <= rY && rY <= this.maxY)
					pixels[y][x] = true;
			}

		return new BooleanMap(pixels);
	}

}
