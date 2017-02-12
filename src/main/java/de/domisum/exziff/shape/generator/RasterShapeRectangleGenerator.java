package de.domisum.exziff.shape.generator;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeRectangleGenerator extends RasterShapeGenerator
{

	// SETTINGS
	private double x1;
	private double y1;
	private double x2;
	private double y2;


	// -------
	// INITIALIZATION
	// -------
	public RasterShapeRectangleGenerator(int width, int height, double x1, double y1, double x2, double y2)
	{
		super(width, height);

		if(x1 < 0 || x1 > 1 || y1 < 0 || y1 > 1 || x2 < 0 || x2 > 1 || y2 < 0 || y2 > 1)
			throw new IllegalArgumentException("All coordinates have to be between 0.0 and 1.0");

		// swap values so that x1 < x2 and y1 < y2
		if(x1 > x2)
		{
			double temp = x1;
			x1 = x2;
			x2 = temp;
		}

		if(y1 > y2)
		{
			double temp = y1;
			y1 = y2;
			y2 = temp;
		}

		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}


	// -------
	// GENERATION
	// -------
	@Override
	public RasterShape generate()
	{
		boolean[][] pixels = new boolean[this.height][this.width];

		for(int y = 0; y < this.height; y++)
			for(int x = 0; x < this.width; x++)
			{
				double rX = x/(double) this.width;
				double rY = y/(double) this.height;

				if(this.x1 <= rX && rX <= this.x2 && this.y1 <= rY && rY <= this.y2)
					pixels[y][x] = true;
			}

		return new RasterShape(pixels);
	}

}
