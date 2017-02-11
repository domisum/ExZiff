package de.domisum.exziff.shape.generator;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeCircleGenerator extends RasterShapeGenerator
{

	// SETTINGS
	private double relativeDiameter;


	// -------
	// INITIALIZATION
	// -------
	public RasterShapeCircleGenerator(int width, int height, double relativeDiameter)
	{
		super(width, height);

		this.relativeDiameter = relativeDiameter;
	}


	// -------
	// GENERATION
	// -------
	@Override
	public RasterShape generate()
	{
		boolean[][] pixels = new boolean[this.height][this.width];
		for(int x = 0; x < this.width; x++)
			for(int y = 0; y < this.height; y++)
			{
				double relXFromCenter = (double) x/Math.min(this.width, this.height)-0.5;
				double relYFromCenter = (double) y/Math.min(this.width, this.height)-0.5;

				if(Math.sqrt(relXFromCenter*relXFromCenter+relYFromCenter*relYFromCenter) < this.relativeDiameter/2)
					pixels[y][x] = true;
			}

		return new RasterShape(pixels);
	}

}
