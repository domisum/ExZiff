package de.domisum.exziff.map;

public class BooleanMap
{

	private final boolean[][] pixels;


	// -------
	// INITIALIZATION
	// -------
	public BooleanMap(boolean[][] pixels)
	{
		if(pixels.length < 1 || pixels[0].length < 1)
			throw new IllegalArgumentException("The map has to have a size of at least 1 in each direction");

		this.pixels = pixels;
	}


	// -------
	// GETTERS
	// -------
	public int getWidth()
	{
		return this.pixels[0].length;
	}

	public int getHeight()
	{
		return this.pixels.length;
	}

	public boolean get(int x, int y)
	{
		return this.pixels[y][x];
	}

}
