package de.domisum.exziff.generator;

import de.domisum.exziff.bedrockregion.BedrockRegionMap;
import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.bedrockregion.blockstack.BlockStack;
import de.domisum.exziff.bedrockregion.blockstack.BlockStackMerger;
import de.domisum.exziff.bedrockregion.regions.BedrockRegion;
import de.domisum.exziff.generator.bedrockpartition.BedrockRegionPartitionGenerator;
import de.domisum.exziff.generator.continentshape.ContinentsShapeGenerator;
import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.converter.BooleanMapToImageConverter;
import de.domisum.exziff.map.converter.ShortMapToImageConverter;
import de.domisum.exziff.map.transformer.bool.BooleanMapScale;
import de.domisum.exziff.map.transformer.bool.BooleanMapSmooth;
import de.domisum.exziff.world.Material;
import de.domisum.exziff.world.World;
import de.domisum.exziff.world.chunkclustersource.ChunkClusterSourceFromDisk;
import de.domisum.lib.auxilium.data.container.AlwaysUnequalDuo;
import de.domisum.lib.auxilium.data.container.Duo;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WorldGenerator
{

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// INPUT
	private int size;
	private long seed;
	private Random random;

	private File worldDirectory;

	// TEMP
	private BooleanMap continentShape;
	private BedrockRegionMap bedrockRegionMap;

	// OUTPUT
	private World world;


	// INIT
	public WorldGenerator(int size, long seed, File worldDirectory)
	{
		this.size = size;
		this.seed = seed;
		this.random = new Random(seed);

		this.worldDirectory = worldDirectory;
	}


	// GENERATION
	public World generate()
	{
		ChunkClusterSourceFromDisk chunkClusterLoaderSaver = new ChunkClusterSourceFromDisk(this.worldDirectory, true);
		this.world = new World(chunkClusterLoaderSaver);

		this.logger.info("Starting world generation...");

		this.logger.info("size: {}", size);
		this.logger.info("seed: {}", seed);


		this.logger.info("Generating continent shape...");
		generateContinentShape();
		this.logger.info("Generating continent shape done.");
		testExportContinentShape();


		this.logger.info("Generating bedrock region partitioning...");
		generateBedrockRegionPartitioning();
		this.logger.info("Generating bedrock region partitioning done");
		testExportRegion();
		ThreadUtil.runAsync(this::testExportBedrockRegionPartitioning, "async task");


		this.logger.info("Generating bedrock regions...");
		generateBedrockRegions();
		this.logger.info("Generating bedrock regions done");


		this.logger.info("Building bedrock...");
		buildBedrock();
		this.logger.info("Building bedrock done");


		this.logger.info("World generation done. Saving world...");

		this.world.save();
		this.logger.info("Saving world done.");
		return this.world;
	}


	private void generateContinentShape()
	{
		int scalingFactor = 4;

		ContinentsShapeGenerator generator = new ContinentsShapeGenerator();
		generator.setDownscalingFactor(scalingFactor);

		this.continentShape = generator.generate(random.nextLong(), size);

		this.logger.info("Scaling continents shape to full size...");
		BooleanMapScale scale = new BooleanMapScale(scalingFactor);
		this.continentShape = scale.transform(this.continentShape);
		this.logger.info("Smoothing upscaled shape...");
		BooleanMapSmooth smooth = new BooleanMapSmooth(2, 0.3, 0.7);
		this.continentShape = smooth.transform(this.continentShape);
	}

	private void generateBedrockRegionPartitioning()
	{
		BedrockRegionPartitionGenerator bedrockRegionPartitionGenerator = new BedrockRegionPartitionGenerator();
		bedrockRegionMap = bedrockRegionPartitionGenerator.generate(random.nextLong(), continentShape);
	}

	private void generateBedrockRegions()
	{
		for(BedrockRegion bedrockRegion : bedrockRegionMap.getBedrockRegions())
			bedrockRegion.generate();
	}

	private void buildBedrock()
	{
		for(int z = 0; z < bedrockRegionMap.getRegionIdMap().getHeight(); z++)
		{
			for(int x = 0; x < bedrockRegionMap.getRegionIdMap().getWidth(); x++)
				buildBedrockAt(x, z);

			System.out.println("buildBedrock: "+z);
		}
	}

	private void buildBedrockAt(int x, int z)
	{
		List<BlockStackMerger.ValuedBlockStack> valuedBlockStacks = new ArrayList<>();
		for(Map.Entry<BedrockRegion, Float> entry : bedrockRegionMap.getInfluencesAt(x, z).entrySet())
			valuedBlockStacks.add(new BlockStackMerger.ValuedBlockStack(entry.getKey().getBlockStackAt(x, z), entry.getValue()));

		BlockStackMerger blockStackMerger = new BlockStackMerger(valuedBlockStacks);
		BlockStack mergedBlockStack = blockStackMerger.merge();

		buildBlockStackAt(mergedBlockStack, x, z);

		for(int y = mergedBlockStack.getMaximumY()+1; y < 45; y++)
			world.setMaterial(x, y, z, Material.WATER);
	}

	private void buildBlockStackAt(BlockStack blockStack, int x, int z)
	{
		for(int y = 0; y <= blockStack.getMaximumY(); y++)
			world.setMaterial(x, y, z, blockStack.getMaterialAt(y));
	}


	private void testExportContinentShape()
	{
		BooleanMapToImageConverter booleanMapToImageConverter = new BooleanMapToImageConverter();
		BufferedImage image = booleanMapToImageConverter.convert(continentShape);
		FileUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//shape.png"), image);
	}

	private void testExportRegion()
	{
		ShortMapToImageConverter shortMapToImageConverter = new ShortMapToImageConverter();
		BufferedImage image = shortMapToImageConverter.convert(bedrockRegionMap.getRegionIdMap());
		FileUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//regions.png"), image);
	}

	private void testExportBedrockRegionPartitioning()
	{
		BufferedImage bedrockRegionMapImage = generateInfluenceMapImage(bedrockRegionMap);
		FileUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\exziff//influence.png"), bedrockRegionMapImage);
	}


	public static BufferedImage generateInfluenceMapImage(BedrockRegionMap bedrockRegionMap)
	{
		int[][] pixels = new int[bedrockRegionMap.getRegionIdMap().getHeight()][bedrockRegionMap.getRegionIdMap().getWidth()];

		for(int y = 0; y < bedrockRegionMap.getRegionIdMap().getHeight(); y++)
		{
			for(int x = 0; x < bedrockRegionMap.getRegionIdMap().getWidth(); x++)
			{
				Color color = getColorAt(bedrockRegionMap, x, y);

				pixels[y][x] = color.getRGB();
			}

			System.out.println("y: "+y);
		}

		return ImageUtil.getImageFromPixels(pixels);
	}

	private static Color getColorAt(BedrockRegionMap bedrockRegionMap, int x, int y)
	{
		Map<BedrockRegion, Float> influencesAt = bedrockRegionMap.getInfluencesAt(x, y);

		Set<Duo<Color, Float>> colorsAndStrength = new HashSet<>();
		for(Map.Entry<BedrockRegion, Float> entry : influencesAt.entrySet())
			colorsAndStrength.add(new AlwaysUnequalDuo<>(getColorForRegion(entry.getKey().getType()), entry.getValue()));

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

	private static Color mix(Set<Duo<Color, Float>> colorsAndStrength)
	{
		double red = 0;
		double green = 0;
		double blue = 0;

		double strengthSum = 0;

		for(Duo<Color, Float> entry : colorsAndStrength)
		{
			red += entry.a.getRed()*entry.b;
			green += entry.a.getGreen()*entry.b;
			blue += entry.a.getBlue()*entry.b;

			strengthSum += entry.b;
		}

		return new Color((int) (red/strengthSum), (int) (green/strengthSum), (int) (blue/strengthSum));
	}

}
