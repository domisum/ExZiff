package de.domisum.exziff.foundation;

public class FoundationRegionBeach extends FoundationRegion
{

	// INIT
	public FoundationRegionBeach(int id, long seed)
	{
		super(FoundationRegionType.BEACH, id, seed);
	}


	// GENERATION
	@Override public void generate()
	{

	}

	@Override public int getHeightAt(int x, int z)
	{
		return 51;
	}

}
