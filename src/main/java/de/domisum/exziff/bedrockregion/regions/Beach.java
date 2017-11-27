package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.map.FloatMap;

public class Beach extends BedrockRegion
{

	// INIT
	public Beach(int id, long seed, FloatMap influenceMap)
	{
		super(BedrockRegionType.BEACH, id, seed, influenceMap);
	}


	// GENERATION
	@Override public void generate()
	{

	}

	@Override public int getHeightAt(int x, int z)
	{
		return 0;
	}

}
