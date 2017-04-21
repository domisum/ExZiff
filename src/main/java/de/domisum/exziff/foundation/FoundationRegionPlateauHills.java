package de.domisum.exziff.foundation;

public class FoundationRegionPlateauHills extends FoundationRegion
{

	// INIT
	public FoundationRegionPlateauHills(int id, long seed)
	{
		super(FoundationRegionType.PLATEAU_HILLS, id, seed);
	}


	// GENERATION
	@Override public void generate()
	{

	}

	@Override public int getHeightAt(int x, int z)
	{
		return 90;
	}

}
