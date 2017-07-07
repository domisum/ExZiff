package de.domisum.exziff.foundation;

public class FoundationRegionOceanFloor extends FoundationRegion
{

	// INIT
	public FoundationRegionOceanFloor(int id, long seed)
	{
		super(FoundationRegionType.OCEAN_FLOOR, id, seed);
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
