package de.domisum.exziff.map.floatmap;

import de.domisum.exziff.map.FloatMap;
import de.domisum.exziff.map.transformer.Transformer;
import de.domisum.lib.auxilium.data.container.math.Coordinate2DInt;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

@RequiredArgsConstructor
public class FloatMapLocalized implements FloatMap
{

	// CONSTANTS
	private static final int EXPAND_BUFFER_DISTANCE = 50;

	//
	@Getter private final int width;
	@Getter private final int height;

	private Coordinate2DInt regionPosition;
	private FloatMap region;


	@Override public float get(int x, int y)
	{
		Validate.inclusiveBetween(0, width-1, x);
		Validate.inclusiveBetween(0, height-1, y);

		if(region == null)
			return 0;

		int inRegionX = x-regionPosition.x;
		int inRegionY = y-regionPosition.y;

		if(inRegionX >= 0 && inRegionX < region.getWidth())
			if(inRegionY >= 0 && inRegionY < region.getHeight())
				return region.get(inRegionX, inRegionY);

		return 0;
	}

	@Override public void set(int x, int y, float value)
	{
		Validate.inclusiveBetween(0, width-1, x);
		Validate.inclusiveBetween(0, height-1, y);


		if(region == null)
			initializeAt(x, y);

		expandIfNecessary(x, y);


		int inRegionX = x-regionPosition.x;
		int inRegionY = y-regionPosition.y;
		region.set(inRegionX, inRegionY, value);
	}


	// REGION
	private void initializeAt(int x, int y)
	{
		region = new FloatMapInMemoryArray(1, 1);
		regionPosition = new Coordinate2DInt(x, y);
	}

	private void expandIfNecessary(int x, int y)
	{
		int inRegionX = x-regionPosition.x;
		int inRegionY = y-regionPosition.y;

		if(inRegionX < 0)
			expandRegionLeft(-inRegionX+EXPAND_BUFFER_DISTANCE);
		else if(inRegionX >= region.getWidth())
			expandRegionRight((inRegionX-region.getWidth()+1)+EXPAND_BUFFER_DISTANCE);

		if(inRegionY < 0)
			expandRegionUp(-inRegionY+EXPAND_BUFFER_DISTANCE);
		else if(inRegionY >= region.getHeight())
			expandRegionDown((inRegionY-region.getHeight()+1)+EXPAND_BUFFER_DISTANCE);
	}


	private void expandRegionLeft(int distance)
	{
		int actualExpandDistance = Math.min(distance, regionPosition.x);

		regionPosition = regionPosition.add(-actualExpandDistance, 0);
		copyRegion(actualExpandDistance, 0, (c)->c.add(actualExpandDistance, 0));
	}

	private void expandRegionRight(int distance)
	{
		int actualExpandDistance = Math.min(distance, width-(region.getWidth()+regionPosition.x));

		copyRegion(actualExpandDistance, 0, (c)->c);
	}

	private void expandRegionUp(int distance)
	{
		int actualExpandDistance = Math.min(distance, regionPosition.y);

		regionPosition = regionPosition.add(0, -actualExpandDistance);
		copyRegion(0, actualExpandDistance, (c)->c.add(0, actualExpandDistance));
	}

	private void expandRegionDown(int distance)
	{
		int actualExpandDistance = Math.min(distance, height-(region.getHeight()+regionPosition.y));

		copyRegion(0, actualExpandDistance, (c)->c);
	}


	private void copyRegion(int regionWidthDelta, int regionHeightDelta, Transformer<Coordinate2DInt> coordinateTransformer)
	{
		FloatMap newRegion = new FloatMapInMemoryArray(region.getWidth()+regionWidthDelta, region.getHeight()+regionHeightDelta);

		for(int y = 0; y < region.getHeight(); y++)
			for(int x = 0; x < region.getWidth(); x++)
			{
				Coordinate2DInt transformed = coordinateTransformer.transform(new Coordinate2DInt(x, y));
				//System.out.println("transformed:"+transformed);
				newRegion.set(transformed.x, transformed.y, region.get(x, y));
			}

		region = newRegion;
	}

}
