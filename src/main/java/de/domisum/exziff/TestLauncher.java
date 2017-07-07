package de.domisum.exziff;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.exporter.bool.BooleanMapImageExporter;
import de.domisum.exziff.shape.ContinentsShapeGenerator;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		Random random = new Random(32);

		FileUtil.deleteDirectoryContents(new File("C:\\Users\\domisum\\testChamber/continents/"));
		for(int i = 0; i < 30; i++)
			generate(random.nextLong());
	}

	private static void generate(long seed)
	{
		ContinentsShapeGenerator generator = new ContinentsShapeGenerator((int) Math.pow(2, 14), seed);
		generator.setDownscalingFactor(4*4);
		BooleanMap booleanMap = generator.generate();
		System.out.println("generator done: "+seed);

		BooleanMapImageExporter exporter = new BooleanMapImageExporter();
		BufferedImage image = exporter.export(booleanMap);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber/continents/"+seed+".png"), image);
	}

}
