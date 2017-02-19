package de.domisum.exziff.shape.exporter;

import de.domisum.exziff.shape.ShapeMap;

public abstract class ShapeMapExporter<T>
{

	// -------
	// EXPORT
	// -------
	public abstract T export(ShapeMap input);

}
