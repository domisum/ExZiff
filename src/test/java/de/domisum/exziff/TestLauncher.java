package de.domisum.exziff;

import de.domisum.exziff.bedrockregion.BedrockRegionMap;
import de.domisum.exziff.generator.bedrockpartition.BedrockRegionPartitionGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.converter.ShortMapToImageConverter;
import de.domisum.exziff.map.generator.bool.BooleanMapFromImageGenerator;
import de.domisum.lib.auxilium.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class TestLauncher
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	public static void main(String[] args)
	{
		new TestLauncher().test();
	}

	private void test()
	{
		logger.info("Starting image loading");
		BooleanMapFromImageGenerator fromImageGenerator = new BooleanMapFromImageGenerator(0.5);
		BooleanMap continentShape = fromImageGenerator
				.generate(FileUtil.readImage(new File("C:\\Users\\domisum\\testChamber\\exziff\\res\\continentShape.png")));
		logger.info("Image loading done");


		Random random = new Random(3881);
		BedrockRegionPartitionGenerator bedrockRegionPartitionGenerator = new BedrockRegionPartitionGenerator();
		BedrockRegionMap bedrockRegionMap = bedrockRegionPartitionGenerator.generate(random.nextLong(), continentShape);


		this.logger.info("Starting export");
		ShortMapToImageConverter shortMapToImageConverter = new ShortMapToImageConverter();
		BufferedImage image = shortMapToImageConverter.convert(bedrockRegionMap.getRegionIdMap());
		FileUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//regions.png"), image);
		this.logger.info("Export done");
	}

}
