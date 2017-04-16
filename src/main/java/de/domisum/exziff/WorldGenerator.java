package de.domisum.exziff;

import de.domisum.exziff.island.shape.IslandShapeGenerator;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.exziff.shape.generator.ShapeMapGenerator;
import de.domisum.exziff.world.World;
import de.domisum.exziff.world.loadersaver.ChunkClusterLoaderSaver;

import java.io.File;

public class WorldGenerator
{

	// INPUT
	private int size;
	private long seed;

	private File worldDirectory;


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
		ShapeMap continentShape = generateContinentShape();


		ChunkClusterLoaderSaver chunkClusterLoaderSaver = new ChunkClusterLoaderSaver(this.worldDirectory, true);
		this.world = new World(chunkClusterLoaderSaver);

		this.world.save();
		return this.world;
	}

	private ShapeMap generateContinentShape()
	{
		ShapeMapGenerator generator = new IslandShapeGenerator(this.size, this.size, this.seed*1337L);

		return generator.generate();
	}

}
