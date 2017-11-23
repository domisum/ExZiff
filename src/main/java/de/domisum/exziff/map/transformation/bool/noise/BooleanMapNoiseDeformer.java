package de.domisum.exziff.map.transformation.bool.noise;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.transformation.bool.BooleanMapFloodFill;
import de.domisum.exziff.map.transformation.bool.BooleanMapInvert;
import de.domisum.exziff.map.transformation.bool.BooleanMapSmooth;
import de.domisum.exziff.map.transformation.bool.BooleanMapTransformer;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;

public class BooleanMapNoiseDeformer extends BooleanMapTransformer
{

	// SETTINGS
	private LayeredOpenSimplexNoise noiseX;
	private LayeredOpenSimplexNoise noiseY;

	private int smoothRadius;
	private double removeThreshold;
	private double addThreshold;
	private int smoothIterations;


	// INIT
	public BooleanMapNoiseDeformer(LayeredOpenSimplexNoise noiseX, LayeredOpenSimplexNoise noiseY, int smoothRadius,
			double removeThreshold, double addThreshold, int smoothIterations)
	{
		this.noiseX = noiseX;
		this.noiseY = noiseY;

		this.smoothRadius = smoothRadius;
		this.removeThreshold = removeThreshold;
		this.addThreshold = addThreshold;
		this.smoothIterations = smoothIterations;
	}


	// TRANSFORMATION
	@Override public BooleanMap transform(BooleanMap input)
	{
		BooleanMap deformedShape = input;

		BooleanMapNoiseOffsetter noiseOffsetter = new BooleanMapNoiseOffsetter(this.noiseX, this.noiseY);
		deformedShape = noiseOffsetter.transform(deformedShape);


		BooleanMapSmooth smooth = new BooleanMapSmooth(this.smoothRadius, this.removeThreshold, this.addThreshold,
				this.smoothIterations);
		deformedShape = smooth.transform(deformedShape);

		BooleanMapFloodFill floodFill = new BooleanMapFloodFill();
		deformedShape = floodFill.transform(deformedShape);

		BooleanMapInvert invert = new BooleanMapInvert();
		deformedShape = invert.transform(deformedShape);

		return deformedShape;
	}

}
