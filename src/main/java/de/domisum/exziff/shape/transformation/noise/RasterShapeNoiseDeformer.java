package de.domisum.exziff.shape.transformation.noise;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.transformation.RasterShapeFloodFill;
import de.domisum.exziff.shape.transformation.RasterShapeInvert;
import de.domisum.exziff.shape.transformation.RasterShapeSmooth;
import de.domisum.exziff.shape.transformation.RasterShapeTransformation;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;

public class RasterShapeNoiseDeformer extends RasterShapeTransformation
{

	// SETTINGS
	private LayeredOpenSimplexNoise noiseX;
	private LayeredOpenSimplexNoise noiseY;

	private int smoothRadius;
	private double removeThreshold;
	private double addThreshold;
	private int smoothIterations;


	// -------
	// INIT
	// -------
	public RasterShapeNoiseDeformer(LayeredOpenSimplexNoise noiseX, LayeredOpenSimplexNoise noiseY, int smoothRadius,
			double removeThreshold, double addThreshold, int smoothIterations)
	{
		this.noiseX = noiseX;
		this.noiseY = noiseY;

		this.smoothRadius = smoothRadius;
		this.removeThreshold = removeThreshold;
		this.addThreshold = addThreshold;
		this.smoothIterations = smoothIterations;
	}


	// -------
	// TRANSFORMATION
	// -------
	@Override
	public RasterShape transform(RasterShape input)
	{
		RasterShape deformedShape = input;

		RasterShapeNoiseOffsetter noiseOffsetter = new RasterShapeNoiseOffsetter(this.noiseX, this.noiseY);
		deformedShape = noiseOffsetter.transform(deformedShape);


		RasterShapeSmooth smooth = new RasterShapeSmooth(this.smoothRadius, this.removeThreshold, this.addThreshold,
				this.smoothIterations);
		deformedShape = smooth.transform(deformedShape);

		RasterShapeFloodFill floodFill = new RasterShapeFloodFill();
		deformedShape = floodFill.transform(deformedShape);

		RasterShapeInvert invert = new RasterShapeInvert();
		deformedShape = invert.transform(deformedShape);

		return deformedShape;
	}

}
