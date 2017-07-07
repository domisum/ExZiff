package de.domisum.exziff.shape;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.bool.BooleanMapEllipseGenerator;
import de.domisum.exziff.map.generator.bool.BooleanMapGenerator;
import de.domisum.exziff.map.transformation.bool.BooleanMapFloodFill;
import de.domisum.exziff.map.transformation.bool.BooleanMapInvert;
import de.domisum.exziff.map.transformation.bool.BooleanMapRecenter;
import de.domisum.exziff.map.transformation.bool.BooleanMapSmooth;
import de.domisum.exziff.map.transformation.bool.noise.BooleanMapNoiseDeformer;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.util.Random;

public class IslandShapeGenerator extends BooleanMapGenerator
{

	// SETTINGS
	private int size;
	private long seed;


	// -------
	// INIT
	// -------
	public IslandShapeGenerator(int size, long seed)
	{
		this.size = size;
		this.seed = seed;
	}


	@Override public BooleanMap generate()
	{
		Random r = new Random(this.seed);

		// base shape
		double baseShapeDiameter = RandomUtil.getFromRange(0.4, 0.65, r);
		double baseShapeExcentricity = RandomUtil.getFromRange(0.5, 3, r);
		double baseShapeRotationAngle = RandomUtil.getFromRange(0, Math.PI*2, r);
		BooleanMapEllipseGenerator ellipseGenerator = new BooleanMapEllipseGenerator(this.size, this.size, baseShapeDiameter,
				baseShapeExcentricity, baseShapeRotationAngle);
		BooleanMap baseShape = ellipseGenerator.generate();


		// deform
		Random seedGenerator = new Random(this.seed);
		int deformationIterations = 3;

		BooleanMap deformedShape = baseShape;
		for(int iteration = 1; iteration <= deformationIterations; iteration++)
			deformedShape = deform(deformedShape, iteration, seedGenerator.nextLong());


		// cleanup
		BooleanMapSmooth smooth = new BooleanMapSmooth(2, 0.3, 0.35);
		deformedShape = smooth.transform(deformedShape);

		BooleanMapFloodFill floodFill = new BooleanMapFloodFill();
		deformedShape = floodFill.transform(deformedShape);

		BooleanMapInvert invert = new BooleanMapInvert();
		deformedShape = invert.transform(deformedShape);


		return deformedShape;
	}

	private BooleanMap deform(BooleanMap input, int iteration, long iterationSeed)
	{
		double iterationMultiplier = 1/Math.pow(iteration, 0.7);
		double baseAmplitudeMultiplier = 1/9d;

		// actually deform the shape
		OctavedOpenSimplexNoise noiseX = new OctavedOpenSimplexNoise(5, 0.1, 0.3,
				this.size*baseAmplitudeMultiplier*iterationMultiplier, 0.35, iterationSeed);
		OctavedOpenSimplexNoise noiseY = new OctavedOpenSimplexNoise(5, 0.1, 0.3,
				this.size*baseAmplitudeMultiplier*iterationMultiplier, 0.35, iterationSeed+0xacab88);

		BooleanMapNoiseDeformer deformer = new BooleanMapNoiseDeformer(noiseX, noiseY, 2, 0.0, 0.1, 1);
		BooleanMap deformed = deformer.transform(input);

		// move the shape back into center
		BooleanMapRecenter recenter = new BooleanMapRecenter();
		deformed = recenter.transform(deformed);
		return deformed;
	}

}
