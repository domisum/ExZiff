package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.bedrockregion.blockstack.BlockStack;
import de.domisum.exziff.map.FloatMap;

public class Flatlands extends BedrockRegion
{

	// INIT
	public Flatlands(int id, long seed, FloatMap influenceMap)
	{
		super(BedrockRegionType.FLATLANDS, id, seed, influenceMap);
	}


	// GENERATION
	@Override public void generate()
	{

	}

	@Override public BlockStack getBlockStackAt(int x, int z)
	{
		return null;
	}

}
