package de.domisum.exziff.generator;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.transformation.bool.BooleanMapScale;
import de.domisum.exziff.map.transformation.bool.BooleanMapSmooth;
import de.domisum.exziff.world.World;
import de.domisum.exziff.world.loadersaver.ChunkClusterLoaderSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class WorldGenerator
{

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// INPUT
	private int size;
	private long seed;

	private File worldDirectory;

	// TEMP
	private BooleanMap continentShape;

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

		this.logger.info("Starting world generation...");

		this.logger.info("size: {}", this.size);
		this.logger.info("seed: {}", this.seed);


		// shape
		this.logger.info("Generating continent shape...");
		generateContinentShape();
		this.logger.info("Generating continent shape done.");

		// TODO


		this.logger.info("World generation done. Saving world...");

		this.world.save();
		this.logger.info("Saving world done.");
		return this.world;
	}


	// SHAPE
	private void generateContinentShape()
	{
		int scalingFactor = 4;

		ContinentsShapeGenerator generator = new ContinentsShapeGenerator(this.size, this.seed*1337L);
		generator.setDownscalingFactor(scalingFactor);

		this.continentShape = generator.generate();

		this.logger.info("Scaling continents shape to full size...");
		BooleanMapScale scale = new BooleanMapScale(scalingFactor);
		this.continentShape = scale.transform(this.continentShape);
		this.logger.info("Smoothing upscaled shape...");
		BooleanMapSmooth smooth = new BooleanMapSmooth(2, 0.3, 0.7);
		this.continentShape = smooth.transform(this.continentShape);
	}

}
