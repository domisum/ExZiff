package de.domisum.exziff.test;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.converters.RasterShapeConverterImage;
import de.domisum.exziff.shape.generator.RasterShapeGenerator;
import de.domisum.exziff.shape.generator.RasterShapeGeneratorCircle;
import de.domisum.exziff.shape.transformer.RasterShapeTransformerRecenter;
import de.domisum.exziff.shape.transformer.deformer.RasterShapeTransformerNoiseOffsetter;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.io.File;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		RasterShapeGenerator generator = new RasterShapeGeneratorCircle(1000, 1000, 0.5);
		generator.generate();

		RasterShape shape = generator.getRasterShape();
		for(int i = 0; i < 5; i++)
			shape = transform(shape);

		RasterShapeConverterImage converter = new RasterShapeConverterImage(shape);
		converter.convert();
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\test.png"), converter.getOutput());
	}


	public static RasterShape transform(RasterShape input)
	{
		Random r = new Random();
		long seed = 53330;
		seed = r.nextLong();

		OctavedOpenSimplexNoise octavedOpenSimplexNoiseX = new OctavedOpenSimplexNoise(2, 0.1, 0.3, 1, 0.3, seed);
		OctavedOpenSimplexNoise octavedOpenSimplexNoiseY = new OctavedOpenSimplexNoise(2, 0.1, 0.3, 1, 0.3, seed+34834);
		RasterShapeTransformerNoiseOffsetter offsetter = new RasterShapeTransformerNoiseOffsetter(input, octavedOpenSimplexNoiseX,
				octavedOpenSimplexNoiseY, 50);
		offsetter.transform();

		RasterShapeTransformerRecenter recenter = new RasterShapeTransformerRecenter(offsetter.getOutput());
		recenter.transform();

		return recenter.getOutput();
	}

}
