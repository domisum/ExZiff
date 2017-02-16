package de.domisum.exziff.islandshape;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.generator.RasterShapeCircleGenerator;
import de.domisum.exziff.shape.generator.RasterShapeGenerator;
import de.domisum.exziff.shape.transformation.RasterShapeRecenter;
import de.domisum.exziff.shape.transformation.RasterShapeSmooth;
import de.domisum.exziff.shape.transformation.noise.RasterShapeNoiseDeformer;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.util.Random;

public class IslandShapeGenerator extends RasterShapeGenerator
{

	// SETTINGS
	private long seed;


	// -------
	// INIT
	// -------
	public IslandShapeGenerator(int width, int height, long seed)
	{
		super(width, height);

		this.seed = seed;
	}


	@Override
	public RasterShape generate()
	{
		Random r = new Random(this.seed);

		// base shape
		RasterShapeCircleGenerator circleGenerator = new RasterShapeCircleGenerator(this.width, this.height,
				RandomUtil.distribute(0.4, 0.15, r));
		RasterShape baseShape = circleGenerator.generate();

		// deform
		Random seedGenerator = new Random(this.seed);
		int deformationIterations = 3;

		RasterShape deformedShape = baseShape;
		for(int iteration = 1; iteration <= deformationIterations; iteration++)
			deformedShape = deform(deformedShape, iteration, seedGenerator.nextLong());

		RasterShapeSmooth smooth = new RasterShapeSmooth(2, 0.3, 0.35);
		deformedShape = smooth.transform(deformedShape);

		return deformedShape;
	}

	private RasterShape deform(RasterShape input, int iteration, long iterationSeed)
	{
		double averageSize = (this.width+this.height)/2d;

		double iterationMultiplier = 1/Math.pow(iteration, 0.7);

		// actually deform the shape
		OctavedOpenSimplexNoise noiseX = new OctavedOpenSimplexNoise(5, 0.1, 0.3, averageSize/7*iterationMultiplier, 0.35,
				iterationSeed);
		OctavedOpenSimplexNoise noiseY = new OctavedOpenSimplexNoise(5, 0.1, 0.3, averageSize/7*iterationMultiplier, 0.35,
				iterationSeed+0xacab88);

		RasterShapeNoiseDeformer deformer = new RasterShapeNoiseDeformer(noiseX, noiseY, 2, 0.0, 0.1, 1);
		RasterShape deformed = deformer.transform(input);

		// move the shape back into center
		RasterShapeRecenter recenter = new RasterShapeRecenter();
		deformed = recenter.transform(deformed);
		return deformed;
	}

}
