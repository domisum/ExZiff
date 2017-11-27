package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegionType;

public class OceanFloor extends BedrockRegion
{

	// INIT
	public OceanFloor(int id, long seed)
	{
		super(BedrockRegionType.OCEAN_FLOOR, id, seed);
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
