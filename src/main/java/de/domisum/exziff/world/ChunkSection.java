package de.domisum.exziff.world;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

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
	public static final int NUMBER_OF_BLOCKS = Chunk.WIDTH*Chunk.WIDTH*HEIGHT;

	// DATA
	@Getter private boolean homogenous;
	private byte homogenousMaterialId;
	private byte homogenousMaterialSubId;
	@Getter private byte[] blockData;


	// INIT
	public ChunkSection(byte homogenousMaterialId, byte homogenousMaterialSubId)
	{
		this.homogenous = true;

		this.homogenousMaterialId = homogenousMaterialId;
		this.homogenousMaterialSubId = homogenousMaterialSubId;
	}

	public ChunkSection(byte[] blockData)
	{
		Validate.isTrue(blockData.length == NUMBER_OF_BLOCKS*2, "The length of blockData has to be "+NUMBER_OF_BLOCKS);

		this.homogenous = false;
		this.blockData = blockData;
	}

	private void initBlockData()
	{
		this.homogenous = false;
		this.blockData = new byte[NUMBER_OF_BLOCKS*2];

		// check if the homogenousMaterial is 0, otherwise fill the array with the information
		if(this.homogenousMaterialId != 0 || this.homogenousMaterialSubId != 0)
			for(int i = 0; i < this.blockData.length; i++)
				this.blockData[i] = i%2 == 0 ? this.homogenousMaterialId : this.homogenousMaterialSubId;
	}


	// GETTERS
	public byte getMaterialId(int sX, int sY, int sZ)
	{
		if(isHomogenous())
			return this.homogenousMaterialId;

		int blockInSectionIndex = getBlockInSectionIndex(sX, sY, sZ);
		return this.blockData[blockInSectionIndex*2];
	}

	public byte getMaterialSubId(int sX, int sY, int sZ)
	{
		if(isHomogenous())
			return this.homogenousMaterialSubId;

		int blockInSectionIndex = getBlockInSectionIndex(sX, sY, sZ);
		return this.blockData[blockInSectionIndex*2+1];
	}


	// SETTERS
	public void setMaterialIdAndSubId(int sX, int sY, int sZ, byte materialId, byte materialSubId)
	{
		if(isHomogenous())
		{
			if(this.homogenousMaterialId != materialId || this.homogenousMaterialSubId != materialSubId)
				initBlockData();
			else // the section is already homogenous with the material that should be set, so don't change anything
				return;
		}


		int blockInSectionIndex = getBlockInSectionIndex(sX, sY, sZ);
		this.blockData[blockInSectionIndex*2] = materialId;
		this.blockData[blockInSectionIndex*2+1] = materialSubId;
	}


	// INTERNAL
	private static int getBlockInSectionIndex(int sX, int sY, int sZ)
	{
		int blockInSectionIndex = (sX)+(sZ*Chunk.WIDTH)+sY*(Chunk.WIDTH*Chunk.WIDTH);
		return blockInSectionIndex;
	}

	public void tryMakeHomogenous()
	{
		if(this.homogenous)
			return;

		// if this section is really homogenous, all the blocks have to have the same materialId and materialSubId as the first one
		this.homogenousMaterialId = getMaterialId(0, 0, 0);
		this.homogenousMaterialSubId = getMaterialSubId(0, 0, 0);

		for(int blockIndex = 0; blockIndex < NUMBER_OF_BLOCKS; blockIndex++)
		{
			if(this.blockData[blockIndex*2] != this.homogenousMaterialId)
				return;

			if(this.blockData[blockIndex*2+1] != this.homogenousMaterialSubId)
				return;
		}

		// if no block was different from the first, the chunk section is homogenous
		this.homogenous = true;
		this.blockData = null;
	}

}
