package de.domisum.exziff.foundation;

import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.layeredopensimplexnoise.NoiseLayers;

public class Flatlands extends FoundationRegion
{

	// NOISE
	private LayeredOpenSimplexNoise noise;

	// INIT
	public Flatlands(int id, long seed)
	{
		super(FoundationRegionType.FLATLANDS, id, seed);
	}


	// GENERATION
	@Override public void generate()
	{
		NoiseLayers noiseLayers = new NoiseLayers(new NoiseLayer(50, 7, this.seed*397439834894893L*this.id),
				new NoiseLayer(20, 2.5, this.seed*3848341111L*this.id));

		this.noise = new LayeredOpenSimplexNoise(noiseLayers);
	}

	@Override public int getHeightAt(int x, int z)
	{
		double noiseValue = this.noise.evaluate(x, z);

		return (int) Math.round(55+noiseValue);
	}

}
