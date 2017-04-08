package de.domisum.exziff;

import de.domisum.exziff.heightmap.HeightMap;
import de.domisum.exziff.heightmap.exporter.HeightMapImageExporter;
import de.domisum.exziff.island.heightmap.IslandHeightMapGenerator;
import de.domisum.exziff.island.shape.IslandShapeGenerator;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.exziff.shape.exporter.ShapeMapImageExporter;
import de.domisum.exziff.world.ChunkClusterLoaderSaver;
import de.domisum.exziff.world.Material;
import de.domisum.exziff.world.World;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.math.MathUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
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
		File worldFolder = new File("C:/Users/domisum/testChamber/testWorld");
		FileUtil.deleteDirectory(worldFolder);

		ShapeMap shapeMap = generateIslandShape(5);
		IslandHeightMapGenerator islandHeightMapGenerator = new IslandHeightMapGenerator(shapeMap, 7);
		HeightMap heightMap = islandHeightMapGenerator.generate();

		HeightMapImageExporter exporter = new HeightMapImageExporter();
		BufferedImage image = exporter.export(heightMap);

		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\testIslands/test.png"), image);


		ChunkClusterLoaderSaver loaderSaver = new ChunkClusterLoaderSaver(worldFolder, true);
		World world = new World(loaderSaver);

		LayeredOpenSimplexNoise terraceOffsetNoise = createNoise(6776);


		int waterHeight = 40;
		for(int z = 0; z < 1000; z++)
			for(int x = 0; x < 1000; x++)
			{
				Material mat = shapeMap.get(x, z) ? Material.STONE : Material.WATER;

				double range = (256-waterHeight)/1.8d;
				int height = waterHeight;

				if(shapeMap.get(x, z))
				{
					double rX = x/1000d;
					double rZ = z/1000d;
					double terraceOffsetNoiseValue = terraceOffsetNoise.evaluate(rX, rZ);

					double heightMapValue = heightMap.get(x, z);
					heightMapValue = Math.pow(heightMapValue, 3);
					heightMapValue = terrace(heightMapValue, 0.06, 4);
					heightMapValue += terraceOffsetNoiseValue*(16d/255)*(.5+heightMapValue*.5);

					int terraceHeight = (int) MathUtil.remapLinear(0, 1, 0, range, heightMapValue)+waterHeight;
					int otherHeight = 120;

					double relMixingRadius = 0.1;
					double relDistFromCenter = (x-500d)/1000d;
					double proportionTwo = Math.min(Math.max(.5+(relDistFromCenter*0.5/relMixingRadius), 0), 1);

					height = (int) (terraceHeight*(1-proportionTwo)+otherHeight*proportionTwo);
				}

				for(int y = 0; y <= height; y++)
				{
					if(y == height-2 && mat != Material.WATER)
						mat = Material.DIRT;

					if(y == height && mat != Material.WATER)
						mat = Material.GRASS;

					world.setMaterialIdAndSubId(x, y, z, (byte) mat.id, (byte) 0);
				}
			}

		world.save();
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
