package de.domisum.exziff.map.exporter;

import de.domisum.exziff.map.ShapeMap;

public abstract class ShapeMapExporter<T>
{

	// -------
	// EXPORT
	// -------
	public abstract T export(ShapeMap input);

}
