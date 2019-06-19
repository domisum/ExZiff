package de.domisum.exziff.world;

import de.domisum.exziff.blockstructure.BlockCoordinate;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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
	private Material homogenousMaterial;

	private short[] blockMaterialIds;
	@Getter
	private final Map<BlockCoordinate, Block> blocksWithAttributes = new HashMap<>();


	// INIT
	public ChunkSection()
	{
		this(Material.AIR);
	}

	public ChunkSection(Material homogenousMaterial)
	{
		this.homogenousMaterial = homogenousMaterial;

		if(!homogenousMaterial.blockAttributes.isEmpty())
			throw new IllegalArgumentException("can't create a homogenous chunk section from a block with attributes");
	}

	private void makeHeterogenous()
	{
		blockMaterialIds = new short[NUMBER_OF_BLOCKS];
		for(int i = 0; i < blockMaterialIds.length; i++)
			blockMaterialIds[i] = (byte) homogenousMaterial.ordinal();

		homogenousMaterial = null;
	}


	// GETTERS
	public Block getBlock(int sX, int sY, int sZ)
	{
		Block block = blocksWithAttributes.get(new BlockCoordinate(sX, sY, sZ));
		if(block != null)
			return block;

		BlockBuilder blockBuilder = new BlockBuilder(getMaterial(sX, sY, sZ));
		return blockBuilder.build();
	}

	private Material getMaterial(int sX, int sY, int sZ)
	{
		if(isHomogenous())
			return homogenousMaterial;

		int blockInSectionIndex = getBlockInSectionIndex(sX, sY, sZ);
		return Material.ofId(blockMaterialIds[blockInSectionIndex]);
	}


	public boolean isHomogenous()
	{
		return homogenousMaterial != null;
	}


	// SETTERS
	public void setBlock(int sX, int sY, int sZ, Block block)
	{
		setMaterial(sX, sY, sZ, block.getMaterial());
		if(block.getNumberOfAttributes() > 0)
			blocksWithAttributes.put(new BlockCoordinate(sX, sY, sZ), block);
	}

	private void setMaterial(int sX, int sY, int sZ, Material material)
	{
		if(isHomogenous())
			if(homogenousMaterial != material)
				makeHeterogenous();
			else // the section is already homogenous with the material that should be set, so don't change anything
				return;

		int blockInSectionIndex = getBlockInSectionIndex(sX, sY, sZ);
		blockMaterialIds[blockInSectionIndex] = (short) material.ordinal();
	}


	// INTERNAL
	public static int getBlockInSectionIndex(int sX, int sY, int sZ)
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

		homogenousMaterial = getMaterial(0, 0, 0);
		blockMaterialIds = null;
	}

	private boolean couldBeHomogenous()
	{
		if(!blocksWithAttributes.isEmpty())
			return false;

		// if this section is really homogenous, all the blocks have to have the same material as the first one
		Material homogenousMaterial = getMaterial(0, 0, 0);

		for(int sX = 0; sX < Chunk.WIDTH; sX++)
			for(int sY = 0; sY < HEIGHT; sY++)
				for(int sZ = 0; sZ < Chunk.WIDTH; sZ++)
					if(getMaterial(sX, sY, sZ) != homogenousMaterial)
						return false;

		// if no block was different from the first, the chunk section is homogenous
		return true;
	}

}
