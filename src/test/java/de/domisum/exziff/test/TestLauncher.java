package de.domisum.exziff.test;

import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.exporter.RasterShapeImageExporter;
import de.domisum.exziff.shape.generator.RasterShapeCircleGenerator;
import de.domisum.exziff.shape.generator.RasterShapeGenerator;
import de.domisum.exziff.shape.transformation.RasterShapeFloodFill;
import de.domisum.exziff.shape.transformation.RasterShapeInvert;
import de.domisum.exziff.shape.transformation.RasterShapeRecenter;
import de.domisum.exziff.shape.transformation.RasterShapeSmooth;
import de.domisum.exziff.shape.transformation.noise.RasterShapeNoiseOffsetter;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		Random r = new Random();
		long seed = 5330;
		seed = r.nextLong();

		int size = 1000;

		RasterShapeGenerator generator = new RasterShapeCircleGenerator(size, size, 0.5);
		RasterShape shape = generator.generate();


		OctavedOpenSimplexNoise octavedOpenSimplexNoiseX = new OctavedOpenSimplexNoise(10, 0.1, 0.3, 1, 0.35, seed);
		OctavedOpenSimplexNoise octavedOpenSimplexNoiseY = new OctavedOpenSimplexNoise(10, 0.1, 0.3, 1, 0.35, seed+34834);
		RasterShapeNoiseOffsetter noiseOffsetter = new RasterShapeNoiseOffsetter(octavedOpenSimplexNoiseX,
				octavedOpenSimplexNoiseY, size/10);
		shape = noiseOffsetter.transform(shape);

		RasterShapeRecenter recenter = new RasterShapeRecenter();
		shape = recenter.transform(shape);

		RasterShapeSmooth smooth = new RasterShapeSmooth(2, 0.25, 0.5);
		for(int i = 0; i < 4; i++)
			shape = smooth.transform(shape);

		RasterShapeFloodFill floodfill = new RasterShapeFloodFill();
		RasterShapeInvert invert = new RasterShapeInvert();

		System.out.println("ff start");
		shape = floodfill.transform(shape);
		System.out.println("ff end");

		shape = invert.transform(shape);


		RasterShapeImageExporter exporter = new RasterShapeImageExporter();
		BufferedImage image = exporter.export(shape);

		System.out.println("b4 writing");
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\test.png"), image);
	}

}
