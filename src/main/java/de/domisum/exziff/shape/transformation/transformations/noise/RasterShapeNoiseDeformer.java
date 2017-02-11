package de.domisum.exziff.shape.transformation.transformations.noise;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.transformation.transformations.RasterShapeTransformation;

public class RasterShapeNoiseDeformer extends RasterShapeTransformation
{

	// SETTINS
	private long seed;


	// -------
	// INIT
	// -------
	public RasterShapeNoiseDeformer(long seed)
	{
		this.seed = seed;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public RasterShape transform(RasterShape input)
	{
		return null;
	}

}
