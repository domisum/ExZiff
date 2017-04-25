package de.domisum.exziff.foundation;

public enum FoundationRegionType
{

	// @formatter:off
	OCEAN_FLOOR,
	BEACH,
	PLATEAU_HILLS,
	FLATLANDS
	;
	// @formatter:on


	public FoundationRegion getInstance(int id, long seed)
	{
		if(this == OCEAN_FLOOR)
			return new FoundationRegionOceanFloor(id, seed);
		else if(this == BEACH)
			return new FoundationRegionBeach(id, seed);
		else if(this == PLATEAU_HILLS)
			return new FoundationRegionPlateauHills(id, seed);
		else if(this == FLATLANDS)
			return new FoundationRegionFlatlands(id, seed);
		else
			throw new UnsupportedOperationException("The instance creation of '"+this.name()+"' is not yet implemented");
	}

}
