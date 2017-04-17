package de.domisum.exziff.map;

public class FloatMap
{

	private final float[][] floats;


	// INIT
	public FloatMap(float[][] floats)
	{
		if(floats.length < 1 || floats[0].length < 1)
			throw new IllegalArgumentException("The map has to have a size of at least 1 in each direction");

		this.floats = floats;
	}

	public FloatMap(int width, int height)
	{
		this.floats = new float[height][width];
	}


	// GETTERS
	public int getWidth()
	{
		return this.floats[0].length;
	}

	public int getHeight()
	{
		return this.floats.length;
	}

	public float get(int x, int y)
	{
		return this.floats[y][x];
	}


	// SETTERS
	public void set(int x, int y, float value)
	{
		this.floats[y][x] = value;
	}

}
