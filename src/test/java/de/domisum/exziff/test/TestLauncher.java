package de.domisum.exziff.test;

import de.domisum.exziff.shape.converters.RasterShapeConverterImage;
import de.domisum.exziff.shape.generator.RasterShapeGenerator;
import de.domisum.exziff.shape.generator.RasterShapeGeneratorCircle;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.io.File;

public class TestLauncher
{

	public static void main(String[] args)
	{
		RasterShapeGenerator generator = new RasterShapeGeneratorCircle(1000, 1000, 0.5);
		generator.generate();

		RasterShapeConverterImage converter = new RasterShapeConverterImage(generator.getRasterShape());
		converter.convert();
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber\\test.png"), converter.getOutput());
	}

}
