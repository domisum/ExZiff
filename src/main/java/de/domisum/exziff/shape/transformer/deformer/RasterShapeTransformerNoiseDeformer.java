package de.domisum.exziff.shape.transformer.deformer;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.transformer.RasterShapeTransformer;

public class RasterShapeTransformerNoiseDeformer extends RasterShapeTransformer
{

	// SETTINS
	private long seed;


	// -------
	// INIT
	// -------
	RasterShapeTransformerNoiseDeformer(RasterShape input, long seed)
	{
		super(input);

		this.seed = seed;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public void transform()
	{

	}

}
