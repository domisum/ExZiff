package de.domisum.exziff.heightmap;

public class HeightMap
{

	private final double[][] heights;


	// -------
	// INITIALIZATION
	// -------
	public HeightMap(double[][] heights)
	{
		if(heights.length < 1 || heights[0].length < 1)
			throw new IllegalArgumentException("The map has to have a size of at least 1 in each direction");

		this.heights = heights;
	}


	// -------
	// GETTERS
	// -------
	public int getWidth()
	{
		return this.heights[0].length;
	}

	public int getHeight()
	{
		return this.heights.length;
	}

	public double get(int x, int y)
	{
		return this.heights[y][x];
	}

}
