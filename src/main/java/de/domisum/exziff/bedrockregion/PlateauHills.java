package de.domisum.exziff.bedrockregion;

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
	public PlateauHills(int id, long seed)
	{
		super(BedrockRegionType.PLATEAU_HILLS, id, seed);
	}


	// GENERATION
	@Override public void generate()
	{
		this.heightMultiplier = RandomUtil.getFromRange(0.7, 1.5, this.seedRandom);


		NoiseLayers noiseLayers = new NoiseLayers(new NoiseLayer(70, 30, this.seedRandom.nextLong()),
				new NoiseLayer(30, 10, this.seedRandom.nextLong()), new NoiseLayer(15, 4, this.seedRandom.nextLong()));
		this.noise = new LayeredOpenSimplexNoise(noiseLayers);

		noiseLayers = new NoiseLayers(new NoiseLayer(5, 1, this.seedRandom.nextLong()));
		this.afterTerraceNoise = new LayeredOpenSimplexNoise(noiseLayers);

		noiseLayers = new NoiseLayers(new NoiseLayer(40, 4, this.seedRandom.nextLong()),
				new NoiseLayer(10, 1, this.seedRandom.nextLong()));
		this.stepsNoise = new LayeredOpenSimplexNoise(noiseLayers);
	}

	@Override public int getHeightAt(int x, int z)
	{
		double noiseValue = this.noise.evaluate(x, z)*.8;
		double afterTerraceNoiseValue = this.afterTerraceNoise.evaluate(x, z);
		double stepsNoiseValue = this.stepsNoise.evaluate(x, z);
		double influence = this.influenceMap.get(x, z);


		double additionalHeight = (20+noiseValue)*this.heightMultiplier;
		additionalHeight *= Math.pow(influence, 1.5);

		double doubleHeight = 50+additionalHeight;
		doubleHeight = 256d*terrace(doubleHeight/256d, 40+stepsNoiseValue, .4-0.3*(1-Math.pow(influence, 2)));
		doubleHeight += afterTerraceNoiseValue;

		return (int) Math.round(doubleHeight);
	}


	private static double terrace(double input, double steps, double slopeProportion)
	{
		int interval = (int) Math.floor(input*steps);
		double intervalSize = 1/steps;

		double inInterval = input%intervalSize;
		double slopeInIntervalLength = slopeProportion*intervalSize;

		double inIntervalOutput;
		if(inInterval < slopeInIntervalLength) // slope
		{
			inIntervalOutput = intervalSize*(inInterval/slopeInIntervalLength);
		}
		else // terrace
		{
			inIntervalOutput = intervalSize;
		}

		double output = interval*intervalSize+inIntervalOutput;

		/*System.out.println("in: "+input);
		System.out.println("interval: "+interval);
		System.out.println("intervalSize: "+intervalSize);
		System.out.println("inINterval: "+inInterval);
		System.out.println("slopeInIntLength: "+slopeInIntervalLength);
		System.out.println("inIntervalOutput: "+inIntervalOutput);
		System.out.println("output: "+output);*/
		return output;
	}

}
