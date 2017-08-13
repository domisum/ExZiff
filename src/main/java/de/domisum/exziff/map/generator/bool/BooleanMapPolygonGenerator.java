package de.domisum.exziff.map.generator.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;

import java.util.List;

public class BooleanMapPolygonGenerator extends BooleanMapGenerator
{

	// SETTINGS
	private int size;
	private List<Polygon2D> polygons;


	// INIT
	public BooleanMapPolygonGenerator(int size, List<Polygon2D> polygons)
	{
		this.size = size;
		this.polygons = polygons;
	}


	// GENERATION
	@Override public BooleanMap generate()
	{
		BooleanMap booleanMap = new BooleanMap(this.size, this.size);

		for(int y = 0; y < this.size; y++)
			for(int x = 0; x < this.size; x++)
			{
				double rX = (double) x/this.size;
				double rY = (double) y/this.size;
				Vector2D relPos = new Vector2D(rX, rY);

				for(Polygon2D p : this.polygons)
					if(p.contains(relPos))
					{
						booleanMap.set(x, y, true);
						break;
					}
			}

		return booleanMap;
	}

}
