package de.domisum.exziff.map.floatmap;

import de.domisum.exziff.map.FloatMap;

public class FloatMapInMemoryArray implements FloatMap
{

	private final float[][] floats;


	// INIT
	public FloatMapInMemoryArray(float[][] floats)
	{
		if(floats.length < 1 || floats[0].length < 1)
			throw new IllegalArgumentException("The map has to have a size of at least 1 in each direction");

		this.floats = floats;
	}

	public FloatMapInMemoryArray(int width, int height)
	{
		this.floats = new float[height][width];
	}


	// GETTERS
	@Override public int getWidth()
	{
		return this.floats[0].length;
	}

	@Override public int getHeight()
	{
		return this.floats.length;
	}

	@Override public float get(int x, int y)
	{
		return this.floats[y][x];
	}


	// SETTERS
	@Override public void set(int x, int y, float value)
	{
		this.floats[y][x] = value;
	}

}
