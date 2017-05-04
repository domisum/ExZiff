package de.domisum.exziff;

import de.domisum.exziff.island.shape.IslandShapeGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.shape.exporter.ShapeMapImageExporter;
import de.domisum.exziff.world.World;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		//islandTest();
		//worldTest();

		worldGeneratorTest();
	}

	public static void worldGeneratorTest()
	{
		File worldFolder = new File("C:/Users/domisum/testChamber/testWorld");
		FileUtil.deleteDirectory(worldFolder);

		WorldGenerator worldGenerator = new WorldGenerator(1024*2, 8798989&1823305, worldFolder);
		World world = worldGenerator.generate();
	}


	private static void islandTest()
	{
		FileUtil.deleteDirectoryContents(new File("C:\\Users\\domisum\\testChamber\\testIslands/"));

		Random r = new Random(5);
		for(int i = 0; i < 50; i++)
		{
			long seed = r.nextLong();
			exportIsland(seed+"", seed);
		}
	}

	private static void exportIsland(String fileName, long seed)
	{
		System.out.println("fileName: "+fileName+" seed: "+seed);

		// settings
		int size = 2000;

		// generation
		IslandShapeGenerator islandShapeGenerator = new IslandShapeGenerator(size, size, seed);
		BooleanMap shape = islandShapeGenerator.generate();


		// exporting
		ShapeMapImageExporter exporter = new ShapeMapImageExporter();
		//HeightMapImageExporter exporter = new HeightMapImageExporter();
		BufferedImage image = exporter.export(shape);

		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\testIslands/"+fileName+".png"), image);
	}

	private static BooleanMap generateIslandShape(long seed)
	{
		// settings
		int size = 1000;

		// generation
		IslandShapeGenerator islandShapeGenerator = new IslandShapeGenerator(size, size, seed);
		BooleanMap shape = islandShapeGenerator.generate();

		return shape;
	}

	private static double terrace(double h, double terraceWidth, double numberOfTerraces)
	{
		double W = terraceWidth; // width of terracing bands
		double k = Math.floor(h/W);
		double f = (h-k*W)/W;
		double s = Math.min(numberOfTerraces*f, 1.0);

		return (k+s)*W;
	}

	private static LayeredOpenSimplexNoise createNoise(long seed)
	{
		Random r = new Random(seed);

		NoiseLayer[] layers = new NoiseLayer[10];
		layers[0] = new NoiseLayer(0.01, 0.25, r.nextLong());
		layers[1] = new NoiseLayer(0.04, 0.7, r.nextLong());
		layers[2] = new NoiseLayer(0.005, 0.2, r.nextLong());

		removeNulls:
		{
			int fNull = 0;
			while(layers[fNull] != null)
			{
				if(fNull+1 == layers.length)
					break removeNulls;
				fNull++;
			}

			layers = Arrays.copyOf(layers, fNull);
		}

		LayeredOpenSimplexNoise noise = new LayeredOpenSimplexNoise(layers);
		return noise;
	}

}
