package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.map.FloatMap;

public class OceanFloor extends BedrockRegion
{

	// INIT
	public OceanFloor(int id, long seed, FloatMap influenceMap)
	{
		super(BedrockRegionType.OCEAN_FLOOR, id, seed, influenceMap);
	}


	// GENERATION
	@Override public void generate()
	{
		// for now ocean floor is just flat
	}

	@Override public int getHeightAt(int x, int z)
	{
		return 40;
	}

}
