package de.domisum.exziff.generator;

import de.domisum.exziff.generator.bedrockregionspartition.BedrockRegionsPartitionGenerator;
import de.domisum.exziff.generator.continentshape.ContinentsShapeGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.map.converter.BooleanMapToImageConverter;
import de.domisum.exziff.map.converter.ShortMapToImageConverter;
import de.domisum.exziff.world.Chunk;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.FileUtil.FileType;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

@RequiredArgsConstructor
public class WorldGenerator
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	public static final int WORLD_SIZE = (int) Math.pow(2, 12);

	// DEPENDENCIES
	private final ContinentsShapeGenerator continentsShapeGenerator;
	private final BedrockRegionsPartitionGenerator bedrockRegionPartitionGenerator;


	// GENERATE
	public void generate(File generatorProcessDir, long seed)
	{
		if(!FileUtil.listFilesRecursively(generatorProcessDir, FileType.FILE_AND_DIRECTORY).isEmpty())
			throw new IllegalArgumentException(
					"generatorProcessDir may not contain any files before starting the generator process");
		FileUtil.mkdirs(generatorProcessDir);

		new GenerateMethodObject(generatorProcessDir, seed).generate();
	}

	@RequiredArgsConstructor
	private class GenerateMethodObject
	{

		// INPUT
		private final File generatorProcessDir;
		private final long seed;


		// TEMP
		private Random random;


		// GENERATE
		public void generate()
		{
			logger.info("Starting world generation with seed '{}'", seed);
			logger.info("Generating into directory {}", generatorProcessDir);
			logger.info("World size: {}", WORLD_SIZE);

			BooleanMap continentShape = generateContinentsShape();


			// TODO refactor below this line
			ShortMap partitions = bedrockRegionPartitionGenerator.generate(continentShape, getRandom().nextLong());
			ThreadUtil.createAndStartThread(()->exportBedrockRegionPartitionIllustration(partitions),
					"exportBedrockRegionsPartitionIllustration"
			);
		}

		private BooleanMap generateContinentsShape()
		{
			logger.info("Generating continents shape...");

			BooleanMap continentShape = continentsShapeGenerator.generate(WORLD_SIZE/Chunk.WIDTH, getRandom().nextLong());
			ThreadUtil.createAndStartThread(()->exportContinentsShapeIllustration(continentShape),
					"exportContinentsShapeIllustration"
			);

			logger.info("Generating continents shape done");
			return continentShape;
		}

		private void exportContinentsShapeIllustration(BooleanMap continentShape)
		{
			logger.info("Exporting continents shape illustration...");

			logger.info("Converting continents shape to image");
			BooleanMapToImageConverter booleanMapToImageConverter = new BooleanMapToImageConverter();
			BufferedImage image = booleanMapToImageConverter.convert(continentShape);

			logger.info("Writing continents shape illustration to file");
			FileUtil.writeImage(new File(getIllustrationDirectory(), "continentsShape.png"), image);

			logger.info("Exporting continents shape illustration done");
		}

		private void exportBedrockRegionPartitionIllustration(ShortMap partitions)
		{
			logger.info("Exporting bedrock regions partition illustration...");

			logger.info("Converting bedrock regions partition to image");
			ShortMapToImageConverter shortMapToImageConverter = new ShortMapToImageConverter();
			BufferedImage image = shortMapToImageConverter.convert(partitions);

			logger.info("Writing bedrock regions partition illustration to file");
			FileUtil.writeImage(new File(getIllustrationDirectory(), "regions.png"), image);

			logger.info("Exporting bedrock regions partition illustration done");
		}


		// UTIL
		private Random getRandom()
		{
			if(random == null)
				random = new Random(seed);

			return random;
		}

		private File getIllustrationDirectory()
		{
			return new File(generatorProcessDir, "illustrations");
		}

	}

}
