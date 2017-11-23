package de.domisum.exziff.map.generator.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.exziff.map.generator.Generator;
import de.domisum.lib.auxilium.data.container.math.Polygon2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class BooleanMapFromPolygons implements Generator<Collection<Polygon2D>, BooleanMap>
{

	// SETTINGS
	private final int size;


	// GENERATION
	@Override public BooleanMap generate(Collection<Polygon2D> polygons)
	{
		BooleanMap booleanMap = new BooleanMap(this.size, this.size);

		for(int y = 0; y < this.size; y++)
			for(int x = 0; x < this.size; x++)
			{
				double rX = (double) x/this.size;
				double rY = (double) y/this.size;
				Vector2D relPos = new Vector2D(rX, rY);

				for(Polygon2D p : polygons)
					if(p.contains(relPos))
					{
						booleanMap.set(x, y, true);
						break;
					}
			}

		return booleanMap;
	}

}
