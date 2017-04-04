package de.domisum.exziff;

import de.domisum.exziff.island.heightmap.IslandHeightMapGenerator;
import de.domisum.exziff.island.shape.IslandShapeGenerator;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.exziff.shape.exporter.ShapeMapImageExporter;
import de.domisum.exziff.world.ChunkClusterLoaderSaver;
import de.domisum.exziff.world.World;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		//		islandTest();

		worldTest();
	}


	private static void islandTest()
	{
		try
		{
			FileUtil.deleteDirectoryContents(new File("C:\\Users\\domisum\\testChamber\\testIslands/"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

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
		int size = 1000;

		// generation
		IslandShapeGenerator islandShapeGenerator = new IslandShapeGenerator(size, size, seed);
		ShapeMap shape = islandShapeGenerator.generate();

		IslandHeightMapGenerator heightMapGenerator = new IslandHeightMapGenerator(shape, seed+0x3094);
		//HeightMap heightMap = heightMapGenerator.generate();


		// exporting
		ShapeMapImageExporter exporter = new ShapeMapImageExporter();
		//HeightMapImageExporter exporter = new HeightMapImageExporter();
		BufferedImage image = exporter.export(shape);

		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\testIslands/"+fileName+".png"), image);
	}

	private static void worldTest()
	{
		ChunkClusterLoaderSaver loaderSaver = new ChunkClusterLoaderSaver(new File("C:/Users/domisum/testChamber/testWorld"),
				true);
		World world = new World(loaderSaver);

		long startMs = System.currentTimeMillis();

		for(int i = 0; i < 1000; i++)
			for(int j = 0; j < 1000; j++)
			{
				int dist = RandomUtil.getFromRange(1, 5);
				int max = RandomUtil.getFromRange(50, 60);
				for(int y = 0; y <= max; y += dist)
					world.setMaterialIdAndSubId(i, y, j, (byte) 1, (byte) 0);
			}

		System.out.println("afterAction: "+(System.currentTimeMillis()-startMs));

		world.save();

		System.out.println("afterSave: "+(System.currentTimeMillis()-startMs));
	}

}
