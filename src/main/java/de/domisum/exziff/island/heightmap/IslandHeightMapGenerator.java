package de.domisum.exziff.island.heightmap;

import de.domisum.exziff.heightmap.HeightMap;
import de.domisum.exziff.shape.ShapeMap;
import de.domisum.layeredopensimplexnoise.LayeredOpenSimplexNoise;
import de.domisum.layeredopensimplexnoise.NoiseLayer;

import java.util.Arrays;
import java.util.Random;

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

		double avgSize = (this.islandShape.getWidth()+this.islandShape.getHeight())/2;

		LayeredOpenSimplexNoise noise = createNoise();


		for(int y = 0; y < this.islandShape.getHeight(); y++)
			for(int x = 0; x < this.islandShape.getWidth(); x++)
			{
				if(!this.islandShape.get(x, y))
					continue;

				double rX = x/avgSize;
				double rY = y/avgSize;

				heights[y][x] = noise.evaluate(rX, rY);
			}

		normalize(heights);
		heights = smoothEdges(heights, 15);
		normalize(heights);

		HeightMap islandHeightMap = new HeightMap(heights);
		return islandHeightMap;
	}

	private LayeredOpenSimplexNoise createNoise()
	{
		Random r = new Random(this.seed);

		NoiseLayer[] layers = new NoiseLayer[10];
		layers[0] = new NoiseLayer(0.2, 0.5, r.nextLong());
		layers[1] = new NoiseLayer(0.1, 0.7, r.nextLong());
		layers[2] = new NoiseLayer(0.05, 0.3, r.nextLong());
		layers[3] = new NoiseLayer(0.02, 0.2, r.nextLong());

		removeNulls:
		{
			int fNull = 0;
			while(layers[fNull] != null)
			{
				if(fNull+1 == layers.length)
					break removeNulls;
				fNull++;
			}

			layers = Arrays.copyOf(layers, fNull);
		}

		LayeredOpenSimplexNoise noise = new LayeredOpenSimplexNoise(layers);
		return noise;
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
				if(height == 0)
					continue;

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
				if(height == 0)
					continue;

				height -= min;
				height /= distance;

				heights[y][x] = height;
			}
	}


	private double[][] smoothEdges(double[][] heights, int radius)
	{
		double[][] heightsNew = new double[heights[0].length][heights.length];
		int diameter = (2*radius+1);
		int pixelsInSurrounding = diameter*diameter;

		for(int y = 0; y < this.islandShape.getHeight(); y++)
			for(int x = 0; x < this.islandShape.getWidth(); x++)
			{
				if(!this.islandShape.get(x, y))
				{
					heights[y][x] = 0;
					continue;
				}

				double sum = 0;
				for(int relY = -radius; relY <= radius; relY++)
					for(int relX = -radius; relX <= radius; relX++)
					{
						int cX = x+relX;
						int cY = y+relY;

						if(cX < 0 || cY < 0 || cX >= this.islandShape.getWidth() || cY >= this.islandShape
								.getHeight()) // in bounds?
							continue;

						if(!this.islandShape.get(cX, cY))
							sum++;
					}

				double proportion = sum/(double) pixelsInSurrounding;
				proportion = Math.pow(proportion, 0.5);
				heightsNew[y][x] = heights[y][x]*(1-proportion);
			}

		return heightsNew;
	}
}