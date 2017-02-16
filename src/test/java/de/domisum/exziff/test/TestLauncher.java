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
		islandTest();
	}


	private static void islandTest()
	{
		Random r = new Random();

		for(int i = 0; i < 20; i++)
		{
			long seed = r.nextLong();
			exportIsland(seed+"", seed);
		}
	}

	private static void exportIsland(String fileName, long seed)
	{
		System.out.println("fileName: "+fileName+" seed: "+seed);

		// settings
		int size = 1000;

		// generation
		IslandShapeGenerator islandShapeGenerator = new IslandShapeGenerator(size, size, seed);
		RasterShape shape = islandShapeGenerator.generate();


		// exporting
		RasterShapeImageExporter exporter = new RasterShapeImageExporter();
		BufferedImage image = exporter.export(shape);

		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\testIslands7/"+fileName+".png"), image);
	}

}
