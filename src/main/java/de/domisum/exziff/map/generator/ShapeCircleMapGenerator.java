package de.domisum.exziff.map.generator;

import de.domisum.exziff.map.ShapeMap;

public class ShapeCircleMapGenerator extends ShapeMapGenerator
{

	// SETTINGS
	private double relativeDiameter;


	// -------
	// INITIALIZATION
	// -------
	public ShapeCircleMapGenerator(int width, int height, double relativeDiameter)
	{
		super(width, height);

		this.relativeDiameter = relativeDiameter;
	}


	// -------
	// GENERATION
	// -------
	@Override
	public ShapeMap generate()
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

		return new ShapeMap(pixels);
	}

}
