package de.domisum.exziff.test;

import de.domisum.exziff.islandshape.IslandShapeGenerator;
import de.domisum.exziff.shape.RasterShape;
import de.domisum.exziff.shape.exporter.RasterShapeImageExporter;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		// settings
		Random r = new Random();
		long seed = 5330;
		seed = r.nextLong();

		int size = 1000;


		// generation
		IslandShapeGenerator islandShapeGenerator = new IslandShapeGenerator(size, size, seed);
		RasterShape shape = islandShapeGenerator.generate();


		// exporting
		RasterShapeImageExporter exporter = new RasterShapeImageExporter();
		BufferedImage image = exporter.export(shape);

		System.out.println("b4 writing");
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\test.png"), image);
	}

}
