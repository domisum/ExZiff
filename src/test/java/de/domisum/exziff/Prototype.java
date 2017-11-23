package de.domisum.exziff;

import de.domisum.exziff.generator.bedrockpartition.BedrockRegionPartitionGenerator;
import de.domisum.exziff.generator.bedrockpartition.UniformlyDistributedPointsGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.ShortMap;
import de.domisum.exziff.map.converter.ShortMapToImageConverter;
import de.domisum.exziff.map.generator.bool.BooleanMapFromImageGenerator;
import de.domisum.lib.auxilium.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Prototype
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	public static void start()
	{
		new Prototype().run();
	}

	private void run()
	{
		this.logger.info("Starting image loading");
		BooleanMapFromImageGenerator fromImageGenerator = new BooleanMapFromImageGenerator(0.5);
		BooleanMap continentShape = fromImageGenerator
				.generate(FileUtil.readImage(new File("C:\\Users\\domisum\\testChamber\\exziff\\res\\continentShape.png")));
		this.logger.info("Image loading done");


		Random random = new Random(3881);
		BedrockRegionPartitionGenerator bedrockRegionPartitionGenerator = new BedrockRegionPartitionGenerator(
				new UniformlyDistributedPointsGenerator());
		ShortMap regions = bedrockRegionPartitionGenerator.generate(random.nextLong(), continentShape);


		this.logger.info("Starting export");
		ShortMapToImageConverter shortMapToImageConverter = new ShortMapToImageConverter();
		BufferedImage image = shortMapToImageConverter.convert(regions);
		FileUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//regions.png"), image);
		this.logger.info("Export done");
	}

}
