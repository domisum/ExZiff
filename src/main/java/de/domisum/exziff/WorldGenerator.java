package de.domisum.exziff;

import de.domisum.exziff.foundation.FoundationRegion;
import de.domisum.exziff.foundation.FoundationRegionType;
import de.domisum.exziff.island.shape.IslandShapeGenerator;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.exziff.shape.generator.ShapeMapGenerator;
import de.domisum.exziff.world.World;
import de.domisum.exziff.world.loadersaver.ChunkClusterLoaderSaver;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.math.RandomUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldGenerator
{

	// INPUT
	private int size;
	private long seed;

	private File worldDirectory;


	// TEMP
	private ShapeMap continentShape;

	private Map<Integer, FoundationRegion> foundationRegions = new HashMap<>();
	private ShortMap foundationRegionsMap;


	// OUTPUT
	private World world;


	// INIT
	public WorldGenerator(int size, long seed, File worldDirectory)
	{
		this.size = size;
		this.seed = seed;

		this.worldDirectory = worldDirectory;
	}


	// GENERATION
	public World generate()
	{
		ChunkClusterLoaderSaver chunkClusterLoaderSaver = new ChunkClusterLoaderSaver(this.worldDirectory, true);
		this.world = new World(chunkClusterLoaderSaver);


		// shape
		generateContinentShape();

		// foundation
		generateFoundationRegions();
		blendFoundationRegions();
		buildFoundation();


		this.world.save();
		return this.world;
	}


	// SHAPE
	private void generateContinentShape()
	{
		ShapeMapGenerator generator = new IslandShapeGenerator(this.size, this.size, this.seed*1337L);

		this.continentShape = generator.generate();
	}


	// FOUNDATION
	private void generateFoundationRegions()
	{
		// this is just test code
		int divisions = 16;
		int divisionSize = this.size/divisions;


		for(int i = 1; i <= divisions*divisions; i++)
		{
			FoundationRegion foundationRegion = getRandomFoundationRegion(i);
			this.foundationRegions.put(i, foundationRegion);
		}

		this.foundationRegionsMap = new ShortMap(this.size, this.size);
		for(int z = 0; z < this.size; z++)
			for(int x = 0; x < this.size; x++)
			{
				if(!this.continentShape.get(x, z))
					continue;

				int regionId = (z/divisionSize)*divisions+(x/divisionSize)+1;
				this.foundationRegionsMap.set(x, z, (short) regionId);
			}

		// test export
		exportFoundationRegions();
	}

	private FoundationRegion getRandomFoundationRegion(int id)
	{
		Random random = new Random((this.seed+id)*120014384L);

		FoundationRegionType type = RandomUtil.getElement(FoundationRegionType.values(), random);
		return type.getInstance(id);
	}

	private void blendFoundationRegions()
	{

	}

	private void buildFoundation()
	{

	}


	// test
	private void exportFoundationRegions()
	{
		int minBrightness = 50;

		int[][] pixels = new int[this.size][this.size];

		for(int y = 0; y < this.size; y++)
			for(int x = 0; x < this.size; x++)
			{
				if(!this.continentShape.get(x, y))
					continue;

				int regionId = this.foundationRegionsMap.get(x, y);
				FoundationRegion region = this.foundationRegions.get(regionId);

				int brightnessInt = (regionId*67)%(256-minBrightness)+minBrightness;
				int color = (brightnessInt)|(brightnessInt<<8)|(brightnessInt<<16);

				if(region.getType() == FoundationRegionType.FLATLANDS)
					color = (10)|(250<<8)|(50<<16);
				else if(region.getType() == FoundationRegionType.PLATEAU_HILLS)
					color = (250)|(10<<8)|(10<<16);

				pixels[y][x] = color;
			}

		BufferedImage image = ImageUtil.getImageFromPixels(pixels);
		ImageUtil.writeImage(new File("C:/Users/domisum/testChamber/testWorld.png"), image);
	}

}
