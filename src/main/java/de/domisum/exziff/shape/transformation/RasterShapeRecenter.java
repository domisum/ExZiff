package de.domisum.exziff.shape.transformation;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeRecenter extends RasterShapeTransformation
{

	// -------
	// TRANSFORMATION
	// -------
	@SuppressWarnings("ConstantConditions")
	@Override
	public RasterShape transform(RasterShape input)
	{
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;

		for(int y = 0; y < input.getHeight(); y++)
			for(int x = 0; x < input.getWidth(); x++)
				if(input.get(x, y))
				{
					if(x < minX)
						minX = x;
					if(x > maxX)
						maxX = x;
					if(y < minY)
						minY = y;
					if(y > maxY)
						maxY = y;
				}

		int centerX = (maxX+minX)/2;
		int centerY = (maxY+minY)/2;

		int centerXOffset = centerX-(input.getWidth()/2);
		int centerYOffset = centerY-(input.getHeight()/2);
		RasterShapeShift shifter = new RasterShapeShift(-centerXOffset, -centerYOffset);

		return shifter.transform(input);
	}

}
