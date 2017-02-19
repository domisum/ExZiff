package de.domisum.exziff.shape;

public class ShapeMap
{

	private final boolean[][] pixels;


	// -------
	// INITIALIZATION
	// -------
	public ShapeMap(boolean[][] pixels)
	{
		if(pixels.length < 1 || pixels[0].length < 1)
			throw new IllegalArgumentException("The shape has to have a size of at least 1 in each direction");

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
