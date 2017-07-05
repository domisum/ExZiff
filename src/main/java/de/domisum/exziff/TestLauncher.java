package de.domisum.exziff;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.exporter.bool.BooleanMapImageExporter;
import de.domisum.exziff.shape.BooleanMapContinentsGenerator;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class TestLauncher
{

	public static void main(String[] args)
	{
		//test();

		Random random = new Random(32);

		FileUtil.deleteDirectoryContents(new File("C:\\Users\\domisum\\testChamber/continents/"));
		for(int i = 0; i < 30; i++)
			generate(random.nextLong());
	}

	private static void generate(long seed)
	{
		BooleanMapContinentsGenerator generator = new BooleanMapContinentsGenerator((int) Math.pow(2, 14), seed);
		generator.setDownscalingFactor(4*4);
		BooleanMap booleanMap = generator.generate();
		System.out.println("generator done: "+seed);

		BooleanMapImageExporter exporter = new BooleanMapImageExporter();
		BufferedImage image = exporter.export(booleanMap);
		ImageUtil.writeImage(new File("C:\\Users\\domisum\\testChamber/continents/"+seed+".png"), image);
	}


	private static void test()
	{
		Polygon2D polygon2D = new Polygon2D(new Vector2D(0, 0), new Vector2D(1, 0), new Vector2D(1, 1));

		System.out.println(polygon2D.contains(new Vector2D(0.25, 0.25)));

		System.exit(0);
	}

}
