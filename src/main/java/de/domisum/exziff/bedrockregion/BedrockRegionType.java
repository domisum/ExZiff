package de.domisum.exziff.bedrockregion;

import de.domisum.exziff.bedrockregion.regions.Beach;
import de.domisum.exziff.bedrockregion.regions.BedrockRegion;
import de.domisum.exziff.bedrockregion.regions.Flatlands;
import de.domisum.exziff.bedrockregion.regions.OceanFloor;
import de.domisum.exziff.bedrockregion.regions.PlateauHills;

public enum BedrockRegionType
{

	// @formatter:off
	OCEAN_FLOOR,
	BEACH,
	PLATEAU_HILLS,
	FLATLANDS
	;
	// @formatter:on


	public BedrockRegion getInstance(int id, long seed)
	{
		if(this == OCEAN_FLOOR)
			return new OceanFloor(id, seed);
		else if(this == BEACH)
			return new Beach(id, seed);
		else if(this == PLATEAU_HILLS)
			return new PlateauHills(id, seed);
		else if(this == FLATLANDS)
			return new Flatlands(id, seed);

		throw new UnsupportedOperationException("The instance creation of '"+this.name()+"' is not yet implemented");
	}

}
