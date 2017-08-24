package de.domisum.exziff.foundation;

import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.layeredopensimplexnoise.NoiseLayers;

public class FoundationRegionBeach extends FoundationRegion
{

	// NOISE
	private LayeredOpenSimplexNoise noise;

	// INIT
	public FoundationRegionBeach(int id, long seed)
	{
		super(FoundationRegionType.BEACH, id, seed);
	}


	// GENERATION
	@Override public void generate()
	{
		NoiseLayers noiseLayers = new NoiseLayers(new NoiseLayer(30, 2, this.seed*397439834894L*this.id),
				new NoiseLayer(10, .8, this.seed*384111L*this.id));

		this.noise = new LayeredOpenSimplexNoise(noiseLayers);
	}


	@Override public int getHeightAt(int x, int z)
	{
		double noiseValue = this.noise.evaluate(x, z);

		return (int) Math.round(51+noiseValue);
	}

}
