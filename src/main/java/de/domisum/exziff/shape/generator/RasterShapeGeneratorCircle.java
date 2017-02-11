package de.domisum.exziff.shape.generator;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeGeneratorCircle extends RasterShapeGenerator
{

	// SETTINGS
	private double relativeRadius;


	// -------
	// INITIALIZATION
	// -------
	public RasterShapeGeneratorCircle(int width, int height, double relativeRadius)
	{
		super(width, height);

		this.relativeRadius = relativeRadius;
	}


	// -------
	// GENERATION
	// -------
	@Override
	public void generate()
	{
		boolean[][] pixels = new boolean[this.height][this.width];
		for(int x = 0; x < this.width; x++)
			for(int y = 0; y < this.height; y++)
			{
				double relXFromCenter = (double) x/Math.min(this.width, this.height)-0.5;
				double relYFromCenter = (double) y/Math.min(this.width, this.height)-0.5;

				if(Math.sqrt(relXFromCenter*relXFromCenter+relYFromCenter*relYFromCenter) < this.relativeRadius)
					pixels[y][x] = true;
			}

		this.rasterShape = new RasterShape(pixels);
	}

}
