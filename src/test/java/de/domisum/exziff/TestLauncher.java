package de.domisum.exziff;

import de.domisum.exziff.bedrockregion.BedrockRegionMap;
import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.bedrockregion.regions.BedrockRegion;
import de.domisum.exziff.generator.bedrockpartition.BedrockRegionPartitionGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.converter.ShortMapToImageConverter;
import de.domisum.exziff.map.generator.bool.BooleanMapFromImageGenerator;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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


		logger.info("Exporting influence map");
		BufferedImage bedrockRegionMapImage = generateInfluenceMapImage(bedrockRegionMap);
		FileUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//influence.png"), bedrockRegionMapImage);
		logger.info("Done");
	}

	private static BufferedImage generateInfluenceMapImage(BedrockRegionMap bedrockRegionMap)
	{
		int[][] pixels = new int[bedrockRegionMap.getRegionIdMap().getHeight()][bedrockRegionMap.getRegionIdMap().getWidth()];

		for(int y = 0; y < bedrockRegionMap.getRegionIdMap().getHeight(); y++)
			for(int x = 0; x < bedrockRegionMap.getRegionIdMap().getWidth(); x++)
			{
				Color color = getColorAt(bedrockRegionMap, x, y);

				pixels[y][x] = color.getRGB();
			}

		return ImageUtil.getImageFromPixels(pixels);
	}

	private static Color getColorAt(BedrockRegionMap bedrockRegionMap, int x, int y)
	{
		Map<BedrockRegion, Float> influencesAt = bedrockRegionMap.getInfluencesAt(x, y);

		Map<Color, Float> colorsAndStrength = new HashMap<>();
		for(Map.Entry<BedrockRegion, Float> entry : influencesAt.entrySet())
			colorsAndStrength.put(getColorForRegion(entry.getKey().getType()), entry.getValue());

		return mix(colorsAndStrength);
	}

	private static Color getColorForRegion(BedrockRegionType bedrockRegionType)
	{
		if(bedrockRegionType == BedrockRegionType.OCEAN_FLOOR)
			return Color.BLUE;

		if(bedrockRegionType == BedrockRegionType.BEACH)
			return Color.YELLOW;

		if(bedrockRegionType == BedrockRegionType.FLATLANDS)
			return Color.GREEN;

		if(bedrockRegionType == BedrockRegionType.PLATEAU_HILLS)
			return Color.RED;

		return Color.BLACK;
	}

	private static Color mix(Map<Color, Float> colorsAndStrength)
	{
		double red = 0;
		double green = 0;
		double blue = 0;

		double strengthSum = 0;

		for(Map.Entry<Color, Float> entry : colorsAndStrength.entrySet())
		{
			red += entry.getKey().getRed()*entry.getValue();
			green += entry.getKey().getGreen()*entry.getValue();
			blue += entry.getKey().getBlue()*entry.getValue();

			strengthSum += entry.getValue();
		}

		return new Color((int) (red/strengthSum), (int) (green/strengthSum), (int) (blue/strengthSum));
	}

}
