package de.domisum.exziff.foundation;

public enum FoundationRegionType
{

	// @formatter:off
	PLATEAU_HILLS,
	FLATLANDS
	;
	// @formatter:on


	public FoundationRegion getInstance(int id)
	{
		if(this == PLATEAU_HILLS)
			return new FoundationRegionPlateauHills(id);
		else if(this == FLATLANDS)
			return new FoundationRegionFlatlands(id);
		else
			throw new UnsupportedOperationException("The instance creation of '"+this.name()+"' is not yet implemented");
	}

}
