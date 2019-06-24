package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegion;
import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.blockstack.BlockStack;
import de.domisum.exziff.map.FloatMap;
import de.domisum.exziff.world.block.Material;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.layeredopensimplexnoise.NoiseLayers;
import de.domisum.lib.auxilium.util.math.RandomUtil;

public class PlateauHills extends BedrockRegion
{

	// generated settings
	private double heightMultiplier;

	// noise
	private LayeredOpenSimplexNoise noise;
	private LayeredOpenSimplexNoise afterTerraceNoise;
	private LayeredOpenSimplexNoise stepsNoise;


	// INIT
	public PlateauHills(int id, long seed, FloatMap influenceMap)
	{
		super(BedrockRegionType.PLATEAU_HILLS, id, seed, influenceMap);
	}


	// GENERATION
	@Override
	public void preGenerate()
	{
		heightMultiplier = RandomUtil.getFromRange(0.7, 1.5, random);


		NoiseLayers noiseLayers = new NoiseLayers(
				new NoiseLayer(70, 30, random.nextLong()),
				new NoiseLayer(30, 10, random.nextLong()),
				new NoiseLayer(15, 4, random.nextLong())
		);
		noise = new LayeredOpenSimplexNoise(noiseLayers);

		noiseLayers = new NoiseLayers(new NoiseLayer(5, 1, random.nextLong()));
		afterTerraceNoise = new LayeredOpenSimplexNoise(noiseLayers);

		noiseLayers = new NoiseLayers(new NoiseLayer(40, 4, random.nextLong()), new NoiseLayer(10, 1, random.nextLong()));
		stepsNoise = new LayeredOpenSimplexNoise(noiseLayers);
	}


	@Override
	public BlockStack getBlockStackAt(int x, int z)
	{
		double noiseValue = noise.evaluate(x, z)*.8;
		double afterTerraceNoiseValue = afterTerraceNoise.evaluate(x, z);
		double stepsNoiseValue = stepsNoise.evaluate(x, z);
		double influence = influenceMap.get(x, z);


		double additionalHeight = (20+noiseValue)*heightMultiplier;
		additionalHeight *= Math.pow(influence, 1.5);

		double doubleHeight = 50+additionalHeight;
		doubleHeight = 256d*terrace(doubleHeight/256d, 40+stepsNoiseValue, .4-(0.3*(1-Math.pow(influence, 2))));
		doubleHeight += afterTerraceNoiseValue;

		return BlockStack.fromMaterialAndMaxY(Material.STONE, (int) Math.round(doubleHeight));
	}

	private static double terrace(double input, double steps, double slopeProportion)
	{
		int interval = (int) Math.floor(input*steps);
		double intervalSize = 1/steps;

		double inInterval = input%intervalSize;
		double slopeInIntervalLength = slopeProportion*intervalSize;

		double inIntervalOutput;
		if(inInterval < slopeInIntervalLength) // slope
			inIntervalOutput = intervalSize*(inInterval/slopeInIntervalLength);
		else // terrace
			inIntervalOutput = intervalSize;

		double output = (interval*intervalSize)+inIntervalOutput;
		return output;
	}

}
