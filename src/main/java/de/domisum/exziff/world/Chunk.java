package de.domisum.exziff.world;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

/**
 * Represents a chunk of blocks and the data associated with them.
 * <p>
 * The coordinates prefixed with the letter 'c' (cX, cY and cZ) are chunk coordinates,
 * which means they are relative to the base of the chunk.
 * <h1>Internal implementation:</h1>
 * <p>
 * The chunk is divided into 16 sections that are layered on top of each other.
 * If all blocks in a section have the default blockData (materialId = 0 and materialSubId = 0),
 * the section is represented by null.
 * <p>
 * The data for each block is saved in a single short,
 * the first 8 bytes determine the materialId, the last 8 bytes the materialSubId.
 */
@NoArgsConstructor
public class Chunk
{

	// CONSTANTS
	// chunk
	public static final int WIDTH = 16;
	public static final int HEIGHT = 256;

	// section
	public static final int NUMBER_OF_SECTIONS = 16;

	// material id
	private static final short MIN_MATERIAL_ID = 0;
	private static final short MAX_MATERIAL_ID = 256-1;
	private static final short MIN_MATERIAL_SUB_ID = 0;
	private static final short MAX_MATERIAL_SUB_ID = 16-1;

	// DATA
	private ChunkSection[] chunkSections = new ChunkSection[NUMBER_OF_SECTIONS];


	// -------
	// INITIALIZATION
	// -------
	public Chunk(ChunkSection[] chunkSections)
	{
		Validate.isTrue(chunkSections.length == NUMBER_OF_SECTIONS, "The number of chunk sections has to be "+NUMBER_OF_SECTIONS);

		this.chunkSections = chunkSections;
	}


	// -------
	// GETTERS
	// -------
	public short getMaterialId(int cX, int cY, int cZ)
	{
		short blockData = getBlockData(cX, cY, cZ);

		// the materialId is saved to the left 8 bits of the blockData short
		short materialId = (short) (blockData>>8);
		return materialId;
	}

	public short getMaterialSubId(int cX, int cY, int cZ)
	{
		short blockData = getBlockData(cX, cY, cZ);

		// the sub id is saved in the right 8 bits of the blockData short
		short materialSubId = (short) (blockData&0b11111111);
		return materialSubId;
	}


	// -------
	// SETTERS
	// -------
	public void setMaterialIdAndSubId(int cX, int cY, int cZ, short materialId, short materialSubId)
	{
		Validate.inclusiveBetween(MIN_MATERIAL_ID, MAX_MATERIAL_ID, materialId);
		Validate.inclusiveBetween(MIN_MATERIAL_SUB_ID, MAX_MATERIAL_SUB_ID, materialSubId);

		createSectionIfNotExist(cY);
		ChunkSection section = getSection(cY);

		int sectionY = cY%ChunkSection.HEIGHT;
		short blockData = getBlockDataFromMaterialIdAndSubId(materialId, materialSubId);
		section.setBlockData(cX, sectionY, cZ, blockData);
	}


	// INTERNAL
	private static int getSectionId(int cY)
	{
		int sectionId = cY/ChunkSection.HEIGHT;
		return sectionId;
	}

	private ChunkSection getSection(int cY)
	{
		int sectionId = getSectionId(cY);

		ChunkSection section = this.chunkSections[sectionId];
		return section;
	}

	private short getBlockData(int cX, int cY, int cZ)
	{
		ChunkSection section = getSection(cY);
		if(section == null)
			return 0;

		int sectionY = cY%ChunkSection.HEIGHT;
		short blockData = section.getBlockData(cX, sectionY, cZ);
		return blockData;
	}


	private static short getBlockDataFromMaterialIdAndSubId(short materialId, short materialSubId)
	{
		short blockData = (short) (materialId<<8|materialSubId);
		return blockData;
	}


	private void createSectionIfNotExist(int cY)
	{
		int sectionId = getSectionId(cY);
		if(this.chunkSections[sectionId] != null)
			return;

		this.chunkSections[sectionId] = new ChunkSection();
	}

}
