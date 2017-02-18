package de.domisum.exziff.world;

/**
 * Represents a section of a chunk of blocks and the data associated with them.
 * <p>
 * The coordinates prefixed with the letter 's' (sX, sY and sZ) are section coordinates,
 * which means they are relative to the base of the section.
 */
public class ChunkSection
{

	// CONSTANTS
	public static final int HEIGHT = Chunk.HEIGHT/Chunk.NUMBER_OF_SECTIONS;
	private static final int SECTION_SIZE = Chunk.WIDTH*Chunk.WIDTH*HEIGHT;

	// DATA
	private short[] blockData = new short[SECTION_SIZE];


	// -------
	// GETTERS
	// -------
	public short getBlockData(int sX, int sY, int sZ)
	{
		int blockInSectionIndex = getBlockInSectionIndex(sX, sY, sZ);

		short blockData = this.blockData[blockInSectionIndex];
		return blockData;
	}


	// -------
	// SETTERS
	// -------
	public void setBlockData(int sX, int sY, int sZ, short blockData)
	{
		int blockinSectionIndex = getBlockInSectionIndex(sX, sY, sZ);
		this.blockData[blockinSectionIndex] = blockData;
	}


	// INTERNAL
	private static int getBlockInSectionIndex(int sX, int sY, int sZ)
	{
		int blockInSectionIndex = (sX)+(sZ*Chunk.WIDTH)+sY*(Chunk.WIDTH*Chunk.WIDTH);
		return blockInSectionIndex;
	}

}
