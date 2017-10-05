package de.domisum.exziff.bedrockregion;

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
		else
			throw new UnsupportedOperationException("The instance creation of '"+this.name()+"' is not yet implemented");
	}

}
