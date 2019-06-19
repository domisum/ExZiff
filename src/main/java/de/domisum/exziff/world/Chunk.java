package de.domisum.exziff.world;

import de.domisum.exziff.world.block.Block;
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
	@Getter
	private final int cX;
	@Getter
	private final int cZ;
	@Getter
	private ChunkSection[] chunkSections = new ChunkSection[NUMBER_OF_SECTIONS];


	// INIT
	public Chunk(int cX, int cZ, ChunkSection[] chunkSections)
	{
		this.cX = cX;
		this.cZ = cZ;

		Validate.isTrue(chunkSections.length == NUMBER_OF_SECTIONS, "The number of chunk sections has to be "+NUMBER_OF_SECTIONS);
		Validate.noNullElements(chunkSections, "chunkSections can't contain null elements");

		this.chunkSections = chunkSections.clone();
	}

	public Chunk(int cX, int cZ)
	{
		this.cX = cX;
		this.cZ = cZ;

		for(int i = 0; i < NUMBER_OF_SECTIONS; i++)
			chunkSections[i] = new ChunkSection();
	}


	// GETTERS
	public Block getBlock(int icX, int icY, int icZ)
	{
		ChunkSection section = getSection(icY);
		int inSectionY = icY%ChunkSection.HEIGHT;

		return section.getBlock(icX, inSectionY, icZ);
	}

	// SETTERS
	public void setBlock(int icX, int icY, int icZ, Block block)
	{
		ChunkSection section = getSection(icY);
		int inSectionY = icY%ChunkSection.HEIGHT;

		section.setBlock(icX, inSectionY, icZ, block);
	}


	// INTERNAL
	private ChunkSection getSection(int icY)
	{
		int sectionId = icY/ChunkSection.HEIGHT;

		ChunkSection section = chunkSections[sectionId];
		return section;
	}

}
