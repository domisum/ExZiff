package de.domisum.exziff.prototype;

import de.domisum.exziff.PixelCoordinates;

public class RegionCoord extends PixelCoordinates
{

	public final int regionId;
	public final int travelDistance;


	// INIT
	public RegionCoord(int x, int y, int regionId, int travelDistance)
	{
		super(x, y);
		this.regionId = regionId;
		this.travelDistance = travelDistance;
	}

}
