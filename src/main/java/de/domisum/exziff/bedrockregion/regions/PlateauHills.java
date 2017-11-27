package de.domisum.exziff.bedrockregion.regions;

import de.domisum.exziff.bedrockregion.BedrockRegionType;
import de.domisum.exziff.map.FloatMap;

public class PlateauHills extends BedrockRegion
{

	// INIT
	public PlateauHills(int id, long seed, FloatMap influenceMap)
	{
		super(BedrockRegionType.PLATEAU_HILLS, id, seed, influenceMap);
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
