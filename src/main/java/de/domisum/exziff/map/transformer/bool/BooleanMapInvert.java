package de.domisum.exziff.map.transformer.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.transformer.Transformer;

public class BooleanMapInvert implements Transformer<BooleanMap>
{

	// TRANSFORMATION
	@Override
	public BooleanMap transform(BooleanMap input)
	{
		boolean[][] pixels = new boolean[input.getHeight()][input.getWidth()];

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				pixels[y][x] = !input.get(x, y);

		return new BooleanMap(pixels);
	}

}
