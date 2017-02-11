package de.domisum.exziff.shape.exporter;

import de.domisum.exziff.shape.RasterShape;

public abstract class RasterShapeExporter<T>
{

	// -------
	// CONVERSTION
	// -------
	public abstract T export(RasterShape input);

}
