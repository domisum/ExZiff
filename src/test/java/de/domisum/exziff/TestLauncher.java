package de.domisum.exziff;

import de.domisum.exziff.island.heightmap.IslandHeightMapGenerator;
import de.domisum.exziff.island.shape.IslandShapeGenerator;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.exziff.shape.exporter.ShapeMapImageExporter;
import de.domisum.exziff.world.ChunkClusterLoaderSaver;
import de.domisum.exziff.world.Material;
import de.domisum.exziff.world.World;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;

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
		ShapeMap shapeMap = generateIslandShape(5);


		ChunkClusterLoaderSaver loaderSaver = new ChunkClusterLoaderSaver(new File("C:/Users/domisum/testChamber/testWorld"),
				true);
		World world = new World(loaderSaver);

		long startMs = System.currentTimeMillis();

		for(int z = 0; z < 1000; z++)
			for(int x = 0; x < 1000; x++)
			{
				Material mat = shapeMap.get(x, z) ? Material.STONE : Material.WATER;
				int height = shapeMap.get(x, z) ? 70 : 65;

				for(int y = 0; y <= height; y++)
					world.setMaterialIdAndSubId(x, y, z, (byte) mat.id, (byte) 0);
			}

		System.out.println("afterAction: "+(System.currentTimeMillis()-startMs));

		world.save();

		System.out.println("afterSave: "+(System.currentTimeMillis()-startMs));
	}

	private static ShapeMap generateIslandShape(long seed)
	{
		// settings
		int size = 1000;

		// generation
		IslandShapeGenerator islandShapeGenerator = new IslandShapeGenerator(size, size, seed);
		ShapeMap shape = islandShapeGenerator.generate();

		return shape;
	}

}
