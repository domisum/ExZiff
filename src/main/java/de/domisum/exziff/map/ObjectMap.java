package de.domisum.exziff.map;

import org.apache.commons.lang3.Validate;

public class ObjectMap<T>
{

	private final Object[][] objects;


	// INITIALIZATION
	public ObjectMap(T[][] objects)
	{
		this(objects, null);
	}

	public ObjectMap(int width, int height)
	{
		this(new Object[height][width], null);
	}

	private ObjectMap(Object[][] objects, Void unused) // to differentiate signature
	{
		Validate.isTrue(unused == null);
		if(objects.length < 1 || objects[0].length < 1)
			throw new IllegalArgumentException("The map has to have a size of at least 1 in each direction");

		this.objects = objects;
	}


	// GETTERS
	public int getWidth()
	{
		return this.objects[0].length;
	}

	public int getHeight()
	{
		return this.objects.length;
	}

	public T get(int x, int y)
	{
		// noinspection unchecked
		return (T) this.objects[y][x];
	}


	// SETTERS
	public void set(int x, int y, T value)
	{
		this.objects[y][x] = value;
	}

}
