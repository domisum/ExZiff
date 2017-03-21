package de.domisum.exziff.shape.generator;

import de.domisum.exziff.shape.ShapeMap;

public class EllipseShapeMapGenerator extends ShapeMapGenerator
{

	// SETTINGS
	private double relativeDiameter;
	private double excentricity;
	private double rotationAngle;


	// -------
	// INITIALIZATION
	// -------
	public EllipseShapeMapGenerator(int width, int height, double relativeDiameter, double excentricity, double rotationAngle)
	{
		super(width, height);

		this.relativeDiameter = relativeDiameter;
		this.excentricity = excentricity;
		this.rotationAngle = rotationAngle;
	}


	// -------
	// GENERATION
	// -------
	@Override public ShapeMap generate()
	{
		double scaleA = 1+this.excentricity;
		double scaleB = 1;

		boolean[][] pixels = new boolean[this.height][this.width];
		for(int x = 0; x < this.width; x++)
			for(int y = 0; y < this.height; y++)
			{
				double relXFromCenter = (double) x/Math.min(this.width, this.height)-0.5;
				double relYFromCenter = (double) y/Math.min(this.width, this.height)-0.5;


				double distanceToCenter = Math.sqrt(relXFromCenter*relXFromCenter+relYFromCenter*relYFromCenter);
				double pointAngle = Math.atan2(relYFromCenter, relXFromCenter);
				double rotatedAngle = pointAngle-this.rotationAngle;

				double xRotated = Math.cos(rotatedAngle)*distanceToCenter;
				double yRotated = Math.sin(rotatedAngle)*distanceToCenter;


				double xRotatedScaled = xRotated*scaleA;
				double yRotatedScaled = yRotated*scaleB;

				double rotatedDistanceToCenter = Math.sqrt(xRotatedScaled*xRotatedScaled+yRotatedScaled*yRotatedScaled);

				if(rotatedDistanceToCenter < this.relativeDiameter/2)
					pixels[y][x] = true;
			}

		return new ShapeMap(pixels);
	}

}
