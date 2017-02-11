package de.domisum.exziff.test;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.exporter.RasterShapeImageExporter;
import de.domisum.exziff.shape.generator.RasterShapeGenerator;
import de.domisum.exziff.shape.generator.RasterShapeCircleGenerator;
import de.domisum.exziff.shape.transformation.transformations.RasterShapeRecenterer;
import de.domisum.exziff.shape.transformation.transformations.noise.RasterShapeNoiseOffsetter;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;

public class TestLauncher
{

	public static void main(String[] args)
	{
		long seed = 53330;
		OctavedOpenSimplexNoise octavedOpenSimplexNoiseX = new OctavedOpenSimplexNoise(2, 0.1, 0.3, 1, 0.3, seed);
		OctavedOpenSimplexNoise octavedOpenSimplexNoiseY = new OctavedOpenSimplexNoise(2, 0.1, 0.3, 1, 0.3, seed+34834);


		RasterShapeGenerator generator = new RasterShapeCircleGenerator(1000, 1000, 0.5);
		RasterShapeNoiseOffsetter noiseOffsetter = new RasterShapeNoiseOffsetter(octavedOpenSimplexNoiseX,
				octavedOpenSimplexNoiseY, 50);
		RasterShapeRecenterer recenter = new RasterShapeRecenterer();
		RasterShapeImageExporter exporter = new RasterShapeImageExporter();


		RasterShape shape = generator.generate();
		shape = noiseOffsetter.transform(shape);
		shape = recenter.transform(shape);


		BufferedImage image = exporter.export(shape);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\test.png"), image);
	}

}
