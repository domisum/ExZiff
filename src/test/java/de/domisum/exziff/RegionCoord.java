package de.domisum.exziff;

import de.domisum.lib.auxilium.data.container.math.Coordinate2DInt;

public class RegionCoord extends Coordinate2DInt
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
