package de.domisum.exziff.map;

public class ShortMap
{

	private final short[][] shorts;


	// INIT
	public ShortMap(short[][] shorts)
	{
		if(shorts.length < 1 || shorts[0].length < 1)
			throw new IllegalArgumentException("The map has to have a size of at least 1 in each direction");

		this.shorts = shorts;
	}

	public ShortMap(int width, int height)
	{
		this.shorts = new short[height][width];
	}


	// GETTERS
	public int getWidth()
	{
		return this.shorts[0].length;
	}

	public int getHeight()
	{
		return this.shorts.length;
	}

	public short get(int x, int y)
	{
		return this.shorts[y][x];
	}


	// SETTERS
	public void set(int x, int y, short value)
	{
		this.shorts[y][x] = value;
	}

}
