package de.domisum.exziff.island.heightmap;

import de.domisum.exziff.map.HeightMap;
import de.domisum.exziff.map.ShapeMap;

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

		return new HeightMap(heights);
	}

}
