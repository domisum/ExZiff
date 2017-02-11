package de.domisum.exziff.shape.converters;

import de.domisum.exziff.shape.RasterShape;
import lombok.Getter;

public abstract class RasterShapeConverter<T>
{

	// INPUT
	RasterShape rasterShape;

	// OUTPUT
	@Getter protected T output;


	// -------
	// INITIALZATION
	// -------
	RasterShapeConverter(RasterShape rasterShape)
	{
		if(rasterShape == null)
			throw new IllegalArgumentException("rasterShape can't be null");

		this.rasterShape = rasterShape;
	}


	// -------
	// CONVERSTION
	// -------
	public abstract void convert();

}
