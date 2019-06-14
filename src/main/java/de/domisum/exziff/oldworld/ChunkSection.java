package de.domisum.exziff.oldworld;

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
	private byte homogenousMaterialId = -1;
	private byte homogenousMaterialSubId = -1;
	@Getter
	private byte[] blockData;


	// INIT
	public ChunkSection(byte homogenousMaterialId, byte homogenousMaterialSubId)
	{
		this.homogenousMaterialId = homogenousMaterialId;
		this.homogenousMaterialSubId = homogenousMaterialSubId;
	}

	public ChunkSection(byte[] blockData)
	{
		Validate.isTrue(blockData.length == (NUMBER_OF_BLOCKS*2), "The length of blockData has to be "+NUMBER_OF_BLOCKS);

		this.blockData = blockData.clone();
	}

	private void makeHeterogenous()
	{
		this.blockData = new byte[NUMBER_OF_BLOCKS*2];
		for(int i = 0; i < this.blockData.length; i++)
			this.blockData[i] = ((i%2) == 0) ? this.homogenousMaterialId : this.homogenousMaterialSubId;

		this.homogenousMaterialId = -1;
		this.homogenousMaterialSubId = -1;
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
		return this.blockData[(blockInSectionIndex*2)+1];
	}


	public boolean isHomogenous()
	{
		return this.homogenousMaterialId != -1;
	}


	// SETTERS
	public void setMaterialIdAndSubId(int sX, int sY, int sZ, byte materialId, byte materialSubId)
	{
		if(isHomogenous())
			if((this.homogenousMaterialId != materialId) || (this.homogenousMaterialSubId != materialSubId))
				makeHeterogenous();
			else // the section is already homogenous with the material that should be set, so don't change anything
				return;


		int blockInSectionIndex = getBlockInSectionIndex(sX, sY, sZ);
		this.blockData[blockInSectionIndex*2] = materialId;
		this.blockData[(blockInSectionIndex*2)+1] = materialSubId;
	}


	// INTERNAL
	private static int getBlockInSectionIndex(int sX, int sY, int sZ)
	{
		int blockInSectionIndex = sX+(sZ*Chunk.WIDTH)+(sY*Chunk.WIDTH*Chunk.WIDTH);
		return blockInSectionIndex;
	}


	public void optimize()
	{
		tryMakeHomogenous();
	}

	private void tryMakeHomogenous()
	{
		if(isHomogenous())
			return;

		if(!couldBeHomogenous())
			return;

		this.homogenousMaterialId = getMaterialId(0, 0, 0);
		this.homogenousMaterialSubId = getMaterialSubId(0, 0, 0);
		this.blockData = null;
	}

	private boolean couldBeHomogenous()
	{
		// if this section is really homogenous, all the blocks have to have the same materialId and materialSubId as the first one
		byte homogenousMaterialId = getMaterialId(0, 0, 0);
		byte homogenousMaterialSubId = getMaterialSubId(0, 0, 0);

		for(int sX = 0; sX < Chunk.WIDTH; sX++)
			for(int sY = 0; sY < HEIGHT; sY++)
				for(int sZ = 0; sZ < Chunk.WIDTH; sZ++)
				{
					if(getMaterialId(sX, sY, sZ) != homogenousMaterialId)
						return false;

					if(getMaterialSubId(sX, sY, sZ) != homogenousMaterialSubId)
						return false;
				}

		// if no block was different from the first, the chunk section is homogenous
		return true;
	}

}
