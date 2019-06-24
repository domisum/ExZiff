package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegion;
import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.blockstack.BlockStack;
import de.domisum.exziff.map.FloatMap;
import de.domisum.exziff.world.block.Material;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;
import de.domisum.layeredopensimplexnoise.NoiseLayers;

public class Beach extends BedrockRegion
{

	// NOISE
	private LayeredOpenSimplexNoise noise;


	// INIT
	public Beach(int id, long seed, FloatMap influenceMap)
	{
		super(BedrockRegionType.BEACH, id, seed, influenceMap);
	}


	// GENERATION
	@Override
	public void preGenerate()
	{
		// @formatter:off
		NoiseLayers noiseLayers = new NoiseLayers(
				new NoiseLayer(30, 2, random.nextLong()*id),
				new NoiseLayer(10, .8, random.nextLong()*id)
		);
		// @formatter:on

		noise = new LayeredOpenSimplexNoise(noiseLayers);
	}

	@Override
	public BlockStack getBlockStackAt(int x, int z)
	{
		double noiseValue = noise.evaluate(x, z);
		return BlockStack.fromMaterialAndMaxY(Material.STONE, (int) Math.round(51+noiseValue));
	}

}
