package de.domisum.exziff.map.generator;

import de.domisum.exziff.map.ShapeMap;

public abstract class ShapeMapGenerator
{

	// SETTINGS
	protected int width;
	protected int height;


	// -------
	// INITIALIZATION
	// -------
	protected ShapeMapGenerator(int width, int height)
	{
		if(width < 1 || height < 1)
			throw new IllegalArgumentException("The width and height have to be at least 1");

		this.width = width;
		this.height = height;
	}


	// -------
	// GENERATION
	// -------
	public abstract ShapeMap generate();

}
