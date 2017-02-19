package de.domisum.exziff.island.heightmap;

import de.domisum.exziff.heightmap.HeightMap;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.OctavedOpenSimplexNoise;

public class IslandHeightMapGenerator
{

	// SETTINGS
	private final ShapeMap islandShape;
	private final long seed;


	// -------
	// INIT
	// -------
	public IslandHeightMapGenerator(ShapeMap islandShape, long seed)
	{
		this.islandShape = islandShape;
		this.seed = seed;
	}


	public HeightMap generate()
	{
		double[][] heights = new double[this.islandShape.getHeight()][this.islandShape.getWidth()];

		LayeredOpenSimplexNoise noise = new OctavedOpenSimplexNoise(5, 0.1, 0.3, 1, 0.35, this.seed);
		for(int y = 0; y < this.islandShape.getHeight(); y++)
			for(int x = 0; x < this.islandShape.getWidth(); x++)
			{
				if(!this.islandShape.get(x, y))
					continue;

				heights[y][x] = noise.evaluate(x, y);
			}

		normalize(heights);

		HeightMap islandHeightMap = new HeightMap(heights);
		return islandHeightMap;
	}

	// TODO move this to own class
	private static void normalize(double[][] heights)
	{
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;

		for(int y = 0; y < heights.length; y++)
			for(int x = 0; x < heights[0].length; x++)
			{
				double height = heights[y][x];
				if(height < min)
					min = height;
				if(height > max)
					max = height;
			}

		double distance = max-min;

		for(int y = 0; y < heights.length; y++)
			for(int x = 0; x < heights[0].length; x++)
			{
				double height = heights[y][x];
				height -= min;
				height /= distance;

				heights[y][x] = height;
			}
	}

}
