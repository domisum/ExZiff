package de.domisum.exziff.shape.exporter;

import de.domisum.exziff.map.BooleanMap;

public abstract class ShapeMapExporter<T>
{

	// -------
	// EXPORT
	// -------
	public abstract T export(BooleanMap input);

}
