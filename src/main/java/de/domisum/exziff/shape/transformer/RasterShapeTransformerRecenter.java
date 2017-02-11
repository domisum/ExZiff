package de.domisum.exziff.shape.transformer;

import de.domisum.exziff.shape.RasterShape;

public class RasterShapeTransformerRecenter extends RasterShapeTransformer
{

	// -------
	// INITIALIZATION
	// -------
	public RasterShapeTransformerRecenter(RasterShape input)
	{
		super(input);
	}


	// -------
	// TRANSFORMATION
	// -------
	@SuppressWarnings("ConstantConditions")
	@Override
	public void transform()
	{
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;

		for(int y = 0; y < this.input.getHeight(); y++)
			for(int x = 0; x < this.input.getWidth(); x++)
				if(this.input.get(x, y))
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

		int centerXOffset = centerX-(this.input.getWidth()/2);
		int centerYOffset = centerY-(this.input.getHeight()/2);

		RasterShapeTransformerShift shifter = new RasterShapeTransformerShift(this.input, -centerXOffset, -centerYOffset);
		shifter.transform();
		this.output = shifter.getOutput();
	}

}
