package de.domisum.exziff.map.generator.bool;

import de.domisum.exziff.map.BooleanMap;
import de.domisum.lib.auxilium.contracts.generator.Generator;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.data.container.math.shape.Polygon2D;
import lombok.AllArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
public class BooleanMapFromPolygons implements Generator<Collection<Polygon2D>, BooleanMap>
{

	// SETTINGS
	private final int size;


	// GENERATION
	@Override
	public BooleanMap generate(Collection<Polygon2D> polygons)
	{
		BooleanMap booleanMap = new BooleanMap(size, size);

		for(int y = 0; y < size; y++)
			for(int x = 0; x < size; x++)
			{
				double rX = (double) x/size;
				double rY = (double) y/size;
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
