package de.domisum.exziff.shape.exporter;

import de.domisum.exziff.shape.RasterShape;

public abstract class RasterShapeExporter<T>
{

	// -------
	// EXPORT
	// -------
	public abstract T export(RasterShape input);

}
