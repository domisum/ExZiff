package de.domisum.exziff.shape.transformer;

import de.domisum.exziff.shape.RasterShape;
import lombok.Getter;

public abstract class RasterShapeTransformer
{

	// INPUT
	protected RasterShape input;

	// OUTPUT
	@Getter protected RasterShape output;


	// -------
	// INITIALZATION
	// -------
	RasterShapeTransformer(RasterShape input)
	{
		if(input == null)
			throw new IllegalArgumentException("input RasterShape can't be null");

		this.input = input;
	}


	// -------
	// TRANSFORMATION
	// -------
	public abstract void transform();

}
