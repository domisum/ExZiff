package de.domisum.exziff.world.transcode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.domisum.exziff.blockstructure.BlockCoordinate;
import de.domisum.exziff.world.Chunk;
import de.domisum.exziff.world.ChunkSection;
import de.domisum.exziff.world.block.Block;
import de.domisum.exziff.world.block.Block.BlockAttribute;
import de.domisum.exziff.world.block.Block.BlockBuilder;
import de.domisum.exziff.world.block.Material;
import de.domisum.lib.auxilium.util.SerializationUtil;

import java.util.Map;
import java.util.Map.Entry;

public class ChunkSectionTranscoder implements Transcoder<ChunkSection>
{

	/*
	Format:

	Encoding type is determined by byte 0.

	If the encoding type is TYPE_HOMOGENOUS:
	- byte 1: materialId of the whole ChunkSection
	- byte 2: materialSubId of the whole ChunkSection

	If the encoding type is TYPE_HETEROGENOUS:
	- byte 1 to last byte: the blockData of the ChunkSelection
	 */

	// CONSTANTS
	private static final byte TYPE_HOMOGENOUS = 1;
	private static final byte TYPE_HETEROGENOUS = 2;

	// DEPENDENCIES
	private final Gson blocksWithAttributesGson = new GsonBuilder()
			.setPrettyPrinting()
			.enableComplexMapKeySerialization()
			.registerTypeAdapter(BlockAttribute.class, new BlockAttributeAdapter())
			.create();


	// ENCODE
	@Override
	public byte[] encode(ChunkSection chunkSection)
	{
		chunkSection.optimize();

		if(chunkSection.isHomogenous())
			return encodeHomogenous(chunkSection);
		else
			return encodeHeterogenous(chunkSection);
	}

	private byte[] encodeHomogenous(ChunkSection chunkSection)
	{
		short homogenousMaterialId = (short) chunkSection.getBlock(0, 0, 0).getMaterial().ordinal();

		byte[] encoded = new byte[3];
		encoded[0] = TYPE_HOMOGENOUS;
		Transcoder.encodeShort(encoded, 1, homogenousMaterialId);

		return encoded;
	}

	private byte[] encodeHeterogenous(ChunkSection chunkSection)
	{
		byte[] blocksWithAttributesSerialized = SerializationUtil.serializeAsJsonString(chunkSection.getBlocksWithAttributes(),
				blocksWithAttributesGson
		);

		byte[] encoded = new byte[1+(ChunkSection.NUMBER_OF_BLOCKS*2)+blocksWithAttributesSerialized.length];
		encoded[0] = TYPE_HETEROGENOUS;
		for(int x = 0; x < Chunk.WIDTH; x++)
			for(int y = 0; y < ChunkSection.HEIGHT; y++)
				for(int z = 0; z < Chunk.WIDTH; z++)
				{
					Block block = chunkSection.getBlock(x, y, z);
					Material material = block.getMaterial();
					short materialId = (short) material.ordinal();

					int startingPosition = 1+(ChunkSection.getBlockInSectionIndex(x, y, z)*2);
					Transcoder.encodeShort(encoded, startingPosition, materialId);
				}
		System.arraycopy(blocksWithAttributesSerialized,
				0,
				encoded,
				1+(ChunkSection.NUMBER_OF_BLOCKS*2),
				blocksWithAttributesSerialized.length
		);

		return encoded;
	}


	// DECODE
	@Override
	public ChunkSection decode(byte[] toDecode)
	{
		byte type = toDecode[0];
		if(type == TYPE_HOMOGENOUS)
			return decodeHomogenous(toDecode);
		else if(type == TYPE_HETEROGENOUS)
			return decodeHeterogenous(toDecode);
		else
			throw new IllegalArgumentException("Unknown section type "+type);
	}

	private ChunkSection decodeHomogenous(byte[] toDecode)
	{
		short homogenousMaterialId = Transcoder.decodeShort(toDecode, 1);
		Material homogenousMaterial = Material.ofId(homogenousMaterialId);

		return new ChunkSection(homogenousMaterial);
	}

	private ChunkSection decodeHeterogenous(byte[] toDecode)
	{
		ChunkSection chunkSection = new ChunkSection();

		for(int x = 0; x < Chunk.WIDTH; x++)
			for(int y = 0; y < ChunkSection.HEIGHT; y++)
				for(int z = 0; z < Chunk.WIDTH; z++)
				{
					int startingPosition = 1+(ChunkSection.getBlockInSectionIndex(x, y, z)*2);
					short materialId = Transcoder.decodeShort(toDecode, startingPosition);
					Material material = Material.ofId(materialId);

					if(!material.blockAttributes.isEmpty()) // these are later set through the block map
						continue;

					chunkSection.setBlock(x, y, z, new BlockBuilder(material).build());
				}

		int blocksWithAttributesSerializedLength = toDecode.length-(1+(ChunkSection.NUMBER_OF_BLOCKS*2));
		byte[] blocksWithAttributesSerialized = new byte[blocksWithAttributesSerializedLength];
		System.arraycopy(toDecode,
				1+(ChunkSection.NUMBER_OF_BLOCKS*2),
				blocksWithAttributesSerialized,
				0,
				blocksWithAttributesSerializedLength
		);
		// noinspection EmptyClass
		Map<BlockCoordinate, Block> map = SerializationUtil.deserializeFromJsonString(blocksWithAttributesSerialized,
				new TypeToken<Map<BlockCoordinate, Block>>() {}.getType(),
				blocksWithAttributesGson
		);
		for(Entry<BlockCoordinate, Block> entry : map.entrySet())
			chunkSection.setBlock(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ(), entry.getValue());

		return chunkSection;
	}

}
