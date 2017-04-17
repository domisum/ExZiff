package de.domisum.exziff.map;

import de.domisum.lib.auxilium.data.container.Duo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiFloatMap
{

	private Map<Integer, FloatMap> maps = new HashMap<>();


	// GETTERS
	public int getWidth()
	{
		if(this.maps.isEmpty())
			throw new IllegalStateException("The multi-map does not contain any maps, so no width or height is specified");

		return this.maps.values().iterator().next().getWidth();
	}

	public int getHeight()
	{
		if(this.maps.isEmpty())
			throw new IllegalStateException("The multi-map does not contain any maps, so no width or height is specified");

		return this.maps.values().iterator().next().getHeight();
	}


	public FloatMap getMap(int key)
	{
		return getMapOrError(key);
	}

	public float get(int x, int y, int key)
	{
		return getMapOrError(key).get(x, y);
	}

	public Set<Duo<Integer, Float>> get(int x, int y)
	{
		Set<Duo<Integer, Float>> values = new HashSet<>();

		for(Map.Entry<Integer, FloatMap> entry : this.maps.entrySet())
		{
			float value = entry.getValue().get(x, y);
			if(value == 0)
				continue;

			values.add(new Duo<>(entry.getKey(), value));
		}

		return values;
	}


	private FloatMap getMapOrError(int key)
	{
		FloatMap map = this.maps.get(key);
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
		// check if the maps have the same size
		if(!this.maps.isEmpty())
		{
			if(getWidth() != map.getWidth() || getHeight() != map.getHeight())
				throw new IllegalArgumentException(
						"The supplied Map has to have the same dimensions as the other Maps in the MultiMap");
		}

		this.maps.put(key, map);
	}

}
