package de.domisum.exziff.shape;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.bool.BooleanMapGenerator;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BooleanMapContinentsGenerator extends BooleanMapGenerator
{

	// settings
	private int size;
	private long seed;

	@Getter @Setter private int downscalingFactor = 1;

	// temp
	private Random random;
	private List<Polygon2D> polygons = new ArrayList<>();

	// output
	private BooleanMap output;


	// INIT
	public BooleanMapContinentsGenerator(int size, long seed)
	{
		this.size = size;
		this.seed = seed;
	}


	// GENERATE
	@Override public BooleanMap generate()
	{
		this.random = new Random(this.seed);

		generatePolygons();
		convertToBooleanMap();

		return this.output;
	}

	private void generatePolygons()
	{
		this.polygons.add(new Polygon2D(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.25), new Vector2D(.75, .75)));
		this.polygons.add(new Polygon2D(new Vector2D(0.1, 0.9), new Vector2D(.1, .4), new Vector2D(0.5, 0.9)));
	}


	// conversion
	private void convertToBooleanMap()
	{
		int downscaledSize = this.size/this.downscalingFactor;
		this.output = new BooleanMap(downscaledSize, downscaledSize);

		for(int y = 0; y < downscaledSize; y++)
			for(int x = 0; x < downscaledSize; x++)
			{
				double rX = (double) x/downscaledSize;
				double rY = (double) y/downscaledSize;
				Vector2D relPos = new Vector2D(rX, rY);

				for(Polygon2D p : this.polygons)
					if(p.contains(relPos))
					{
						this.output.set(x, y, true);
						break;
					}
			}
	}

}
