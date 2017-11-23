package de.domisum.exziff.world;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

/**
 * Represents a chunk of blocks and the data associated with them.
 * <p>
 * The coordinates prefixed with 'ic' (icX, icY and icZ) are in-chunk-coordinates,
 * which means they are relative to the base of the chunk.
 * </p>
 * <hr/>
 * <b>Implementation:</b>
 * The chunk is divided into 16 ChunkSections that are layered on top of each other.
 */
public class Chunk
{

	// CONSTANTS
	// chunk
	public static final int WIDTH = 16;
	public static final int HEIGHT = 256;

	// section
	public static final int NUMBER_OF_SECTIONS = 16;

	// DATA
	@Getter private final int cX;
	@Getter private final int cZ;
	@Getter private ChunkSection[] chunkSections = new ChunkSection[NUMBER_OF_SECTIONS];


	// INIT
	public Chunk(int cX, int cZ, ChunkSection[] chunkSections)
	{
		this.cX = cX;
		this.cZ = cZ;

		Validate.isTrue(chunkSections.length == NUMBER_OF_SECTIONS, "The number of chunk sections has to be "+NUMBER_OF_SECTIONS);
		Validate.noNullElements(chunkSections, "chunkSections can't contain null elements");

		this.chunkSections = chunkSections;
	}

	public Chunk(int cX, int cZ)
	{
		this.cX = cX;
		this.cZ = cZ;

		for(int i = 0; i < NUMBER_OF_SECTIONS; i++)
			this.chunkSections[i] = new ChunkSection((byte) 0, (byte) 0);
	}


	// GETTERS
	public byte getMaterialId(int icX, int icY, int icZ)
	{
		ChunkSection section = getSection(icY);
		int inSectionY = icY%ChunkSection.HEIGHT;

		return section.getMaterialId(icX, inSectionY, icZ);
	}

	public byte getMaterialSubId(int icX, int icY, int icZ)
	{
		ChunkSection section = getSection(icY);
		int inSectionY = icY%ChunkSection.HEIGHT;

		return section.getMaterialSubId(icX, inSectionY, icZ);
	}


	// SETTERS
	public void setMaterialIdAndSubId(int icX, int icY, int icZ, byte materialId, byte materialSubId)
	{
		ChunkSection section = getSection(icY);
		int inSectionY = icY%ChunkSection.HEIGHT;

		section.setMaterialIdAndSubId(icX, inSectionY, icZ, materialId, materialSubId);
	}


	// INTERNAL
	private ChunkSection getSection(int icY)
	{
		int sectionId = icY/ChunkSection.HEIGHT;

		ChunkSection section = this.chunkSections[sectionId];
		return section;
	}

}
