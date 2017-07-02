package de.domisum.exziff;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.exporter.bool.BooleanMapImageExporter;
import de.domisum.exziff.shape.BooleanMapContinentsGenerator;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		Random random = new Random(3003);

		BooleanMapContinentsGenerator generator = new BooleanMapContinentsGenerator((int) Math.pow(2, 14), random.nextLong());
		generator.setDownscalingFactor(4);
		BooleanMap booleanMap = generator.generate();

		BooleanMapImageExporter exporter = new BooleanMapImageExporter();
		BufferedImage image = exporter.export(booleanMap);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber/continents/newTest.png"), image);
	}

}
