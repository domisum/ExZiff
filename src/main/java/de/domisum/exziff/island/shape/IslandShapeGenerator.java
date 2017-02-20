package de.domisum.exziff.island.shape;

import de.domisum.exziff.shape.ShapeMap;
import de.domisum.exziff.shape.generator.ShapeCircleMapGenerator;
import de.domisum.exziff.shape.generator.ShapeMapGenerator;
import de.domisum.exziff.shape.transformation.ShapeMapRecenter;
import de.domisum.exziff.shape.transformation.ShapeMapSmooth;
import de.domisum.exziff.shape.transformation.noise.ShapeMapNoiseDeformer;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.util.Random;

public class IslandShapeGenerator extends ShapeMapGenerator
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
	public ShapeMap generate()
	{
		Random r = new Random(this.seed);

		// base shape
		ShapeCircleMapGenerator circleGenerator = new ShapeCircleMapGenerator(this.width, this.height,
				RandomUtil.distribute(0.4, 0.15, r));
		ShapeMap baseShape = circleGenerator.generate();

		// deform
		Random seedGenerator = new Random(this.seed);
		int deformationIterations = 3;

		ShapeMap deformedShape = baseShape;
		for(int iteration = 1; iteration <= deformationIterations; iteration++)
			deformedShape = deform(deformedShape, iteration, seedGenerator.nextLong());

		ShapeMapSmooth smooth = new ShapeMapSmooth(2, 0.3, 0.35);
		deformedShape = smooth.transform(deformedShape);

		return deformedShape;
	}

	private ShapeMap deform(ShapeMap input, int iteration, long iterationSeed)
	{
		double averageSize = (this.width+this.height)/2d;

		double iterationMultiplier = 1/Math.pow(iteration, 0.7);

		// actually deform the shape
		OctavedOpenSimplexNoise noiseX = new OctavedOpenSimplexNoise(5, 0.1, 0.3, averageSize/7*iterationMultiplier, 0.35,
				iterationSeed);
		OctavedOpenSimplexNoise noiseY = new OctavedOpenSimplexNoise(5, 0.1, 0.3, averageSize/7*iterationMultiplier, 0.35,
				iterationSeed+0xacab88);

		ShapeMapNoiseDeformer deformer = new ShapeMapNoiseDeformer(noiseX, noiseY, 2, 0.0, 0.1, 1);
		ShapeMap deformed = deformer.transform(input);

		// move the shape back into center
		ShapeMapRecenter recenter = new ShapeMapRecenter();
		deformed = recenter.transform(deformed);
		return deformed;
	}

}
