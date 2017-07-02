package de.domisum.exziff.map.generator.bool;

import de.domisum.exziff.map.BooleanMap;

public class BooleanMapCircleGenerator extends BooleanMapGenerator
{

	// SETTINGS
	private double relativeDiameter;


	// -------
	// INITIALIZATION
	// -------
	public BooleanMapCircleGenerator(int width, int height, double relativeDiameter)
	{
		super(width, height);

		this.relativeDiameter = relativeDiameter;
	}


	// -------
	// GENERATION
	// -------
	@Override
	public BooleanMap generate()
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

		return new BooleanMap(pixels);
	}

}
