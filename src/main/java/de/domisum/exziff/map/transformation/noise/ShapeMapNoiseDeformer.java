package de.domisum.exziff.map.transformation.noise;

import de.domisum.exziff.map.ShapeMap;
import de.domisum.exziff.map.transformation.ShapeMapMapFloodFill;
import de.domisum.exziff.map.transformation.ShapeMapMapInvert;
import de.domisum.exziff.map.transformation.ShapeMapSmooth;
import de.domisum.exziff.map.transformation.ShapeMapTransformation;
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
	public ShapeMap transform(ShapeMap input)
	{
		ShapeMap deformedShape = input;

		ShapeMapNoiseOffsetter noiseOffsetter = new ShapeMapNoiseOffsetter(this.noiseX, this.noiseY);
		deformedShape = noiseOffsetter.transform(deformedShape);


		ShapeMapSmooth smooth = new ShapeMapSmooth(this.smoothRadius, this.removeThreshold, this.addThreshold,
				this.smoothIterations);
		deformedShape = smooth.transform(deformedShape);

		ShapeMapMapFloodFill floodFill = new ShapeMapMapFloodFill();
		deformedShape = floodFill.transform(deformedShape);

		ShapeMapMapInvert invert = new ShapeMapMapInvert();
		deformedShape = invert.transform(deformedShape);

		return deformedShape;
	}

}
