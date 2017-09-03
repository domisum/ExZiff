package de.domisum.exziff.map.transformation.bool;

import de.domisum.exziff.map.BooleanMap;

public class BooleanMapInvert extends BooleanMapTransformation
{

	// TRANSFORMATION
	@Override public BooleanMap transform(BooleanMap input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				pixels[y][x] = !input.get(x, y);

		return new BooleanMap(pixels);
	}

}
