package de.domisum.exziff.islandshape;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.generator.RasterShapeCircleGenerator;
import de.domisum.exziff.shape.generator.RasterShapeGenerator;
import de.domisum.exziff.shape.transformation.RasterShapeRecenter;
import de.domisum.exziff.shape.transformation.noise.RasterShapeNoiseDeformer;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;

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
		RasterShapeCircleGenerator circleGenerator = new RasterShapeCircleGenerator(this.width, this.height, 0.5);
		RasterShape baseShape = circleGenerator.generate();


		Random seedGenerator = new Random(this.seed);
		int deformationIterations = 3;

		RasterShape deformedShape = baseShape;
		for(int iteration = 1; iteration <= deformationIterations; iteration++)
			deformedShape = deform(deformedShape, iteration, seedGenerator.nextLong());


		return deformedShape;
	}

	private RasterShape deform(RasterShape input, int iteration, long iterationSeed)
	{
		OctavedOpenSimplexNoise noiseX = new OctavedOpenSimplexNoise(5, 0.1, 0.3, 100, 0.35, iterationSeed);
		OctavedOpenSimplexNoise noiseY = new OctavedOpenSimplexNoise(5, 0.1, 0.3, 100, 0.35, iterationSeed+0xacab88);

		RasterShapeNoiseDeformer deformer = new RasterShapeNoiseDeformer(noiseX, noiseY, 2, 0.2, 0.5);
		RasterShape deformed = deformer.transform(input);


		RasterShapeRecenter recenter = new RasterShapeRecenter();
		deformed = recenter.transform(deformed);
		return deformed;
	}

}
