package de.domisum.exziff.shape.transformation.noise;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.shape.transformation.ShapeMapFloodFill;
import de.domisum.exziff.shape.transformation.ShapeMapInvert;
import de.domisum.exziff.shape.transformation.ShapeMapSmooth;
import de.domisum.exziff.shape.transformation.ShapeMapTransformation;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;

public class ShapeMapNoiseDeformer extends ShapeMapTransformation
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
	public ShapeMapNoiseDeformer(LayeredOpenSimplexNoise noiseX, LayeredOpenSimplexNoise noiseY, int smoothRadius,
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
	public BooleanMap transform(BooleanMap input)
	{
		BooleanMap deformedShape = input;

		ShapeMapNoiseOffsetter noiseOffsetter = new ShapeMapNoiseOffsetter(this.noiseX, this.noiseY);
		deformedShape = noiseOffsetter.transform(deformedShape);


		ShapeMapSmooth smooth = new ShapeMapSmooth(this.smoothRadius, this.removeThreshold, this.addThreshold,
				this.smoothIterations);
		deformedShape = smooth.transform(deformedShape);

		ShapeMapFloodFill floodFill = new ShapeMapFloodFill();
		deformedShape = floodFill.transform(deformedShape);

		ShapeMapInvert invert = new ShapeMapInvert();
		deformedShape = invert.transform(deformedShape);

		return deformedShape;
	}

}
