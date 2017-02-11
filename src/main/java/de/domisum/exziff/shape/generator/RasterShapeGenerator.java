package de.domisum.exziff.shape.generator;

import de.domisum.exziff.shape.RasterShape;

public abstract class RasterShapeGenerator
{

	// SETTINGS
	protected int width;
	protected int height;


	// -------
	// INITIALIZATION
	// -------
	protected RasterShapeGenerator(int width, int height)
	{
		if(width < 1 || height < 1)
			throw new IllegalArgumentException("The width and height have to be at least 1");

		this.width = width;
		this.height = height;
	}


	// -------
	// GENERATION
	// -------
	public abstract RasterShape generate();

}
