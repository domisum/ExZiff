package de.domisum.exziff.test;

import de.domisum.exziff.shape.converters.RasterShapeConverterImage;
import de.domisum.exziff.shape.generator.RasterShapeGenerator;
import de.domisum.exziff.shape.generator.RasterShapeGeneratorCircle;
import de.domisum.exziff.shape.transformer.RasterShapeTransformerRecenter;
import de.domisum.exziff.shape.transformer.deformer.RasterShapeTransformerNoiseOffsetter;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.io.File;

public class TestLauncher
{

	public static void main(String[] args)
	{
		long seed = 53330;

		RasterShapeGenerator generator = new RasterShapeGeneratorCircle(1000, 1000, 0.5);
		generator.generate();

		OctavedOpenSimplexNoise octavedOpenSimplexNoiseX = new OctavedOpenSimplexNoise(2, 0.1, 0.3, 1, 0.3, seed);
		OctavedOpenSimplexNoise octavedOpenSimplexNoiseY = new OctavedOpenSimplexNoise(2, 0.1, 0.3, 1, 0.3, seed+34834);
		RasterShapeTransformerNoiseOffsetter offsetter = new RasterShapeTransformerNoiseOffsetter(generator.getRasterShape(),
				octavedOpenSimplexNoiseX, octavedOpenSimplexNoiseY, 50);
		offsetter.transform();

		RasterShapeTransformerRecenter recenter = new RasterShapeTransformerRecenter(offsetter.getOutput());
		recenter.transform();

		RasterShapeConverterImage converter = new RasterShapeConverterImage(recenter.getOutput());
		converter.convert();
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\test.png"), converter.getOutput());
	}

}
