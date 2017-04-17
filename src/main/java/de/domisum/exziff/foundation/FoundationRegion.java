package de.domisum.exziff.foundation;

import lombok.Getter;

public abstract class FoundationRegion
{

	@Getter private final FoundationRegionType type;
	@Getter private final int id;


	// INIT
	public FoundationRegion(FoundationRegionType type, int id)
	{
		this.type = type;
		this.id = id;
	}

}
