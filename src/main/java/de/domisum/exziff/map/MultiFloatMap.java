package de.domisum.exziff.map;

import de.domisum.lib.auxilium.data.container.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MultiFloatMap
{

	private final Map<Integer, FloatMap> maps = new HashMap<>();


	// GETTERS
	public int getWidth()
	{
		if(maps.isEmpty())
			throw new IllegalStateException("The multi-map does not contain any maps, so no width or height is specified");

		return maps.values().iterator().next().getWidth();
	}

	public int getHeight()
	{
		if(maps.isEmpty())
			throw new IllegalStateException("The multi-map does not contain any maps, so no width or height is specified");

		return maps.values().iterator().next().getHeight();
	}


	public FloatMap getMap(int key)
	{
		return getMapOrError(key);
	}

	public float get(int x, int y, int key)
	{
		return getMapOrError(key).get(x, y);
	}

	public Set<Pair<Integer, Float>> get(int x, int y)
	{
		Set<Pair<Integer, Float>> values = new HashSet<>();

		for(Entry<Integer, FloatMap> entry : maps.entrySet())
		{
			float value = entry.getValue().get(x, y);
			if(value == 0)
				continue;

			values.add(new Pair<>(entry.getKey(), value));
		}

		return values;
	}


	private FloatMap getMapOrError(int key)
	{
		FloatMap map = maps.get(key);
		if(map == null)
			throw new IllegalArgumentException("The multimap does not contain a map with the key '"+key+"'");

		return map;
	}


	// SETTERS
	public void set(int x, int y, int key, float value)
	{
		getMapOrError(key).set(x, y, value);
	}

	public void setMap(int key, FloatMap map)
	{
		// check if the new map has the same size as the other maps, if there are any
		if(!maps.isEmpty())
			if((getWidth() != map.getWidth()) || (getHeight() != map.getHeight()))
				throw new IllegalArgumentException(
						"The supplied Map has to have the same dimensions as the other Maps in the MultiMap");

		maps.put(key, map);
	}

}
