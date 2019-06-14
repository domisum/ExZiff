package de.domisum.exziff.world.block;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Block
{

	@Getter
	private final Material material;
	private final Set<BlockAttribute<?>> attributes;


	// INIT
	@RequiredArgsConstructor
	public static class BlockBuilder
	{

		private final Material material;
		private Set<BlockAttribute<?>> attributes;


		public <T> BlockBuilder set(Class<T> type, T value)
		{
			if(attributes == null)
				attributes = new HashSet<>();

			BlockAttribute<T> blockAttribute = new BlockAttribute<>(type, value);
			attributes.add(blockAttribute);

			return this;
		}

		// BUILD
		public Block build()
		{
			checkAllAttributesSet();
			return new Block(material, attributes);
		}

		private void checkAllAttributesSet()
		{
			for(Class<?> attribute : material.blockAttributes)
				checkAttributeSet(attribute);
		}

		private void checkAttributeSet(Class<?> attribute)
		{
			if(attributes != null)
				for(BlockAttribute<?> blockAttribute : attributes)
					if(blockAttribute.getType() == attribute)
						return;

			throw new IllegalStateException(
					"attribute '"+attribute.getSimpleName()+"' of block with material '"+material+"'not set");
		}

	}


	// GETTERS
	public <T> T getAttribute(Class<T> type)
	{
		for(BlockAttribute<?> attribute : attributes)
			if(attribute.getType() == type)
				// noinspection unchecked
				return (T) attribute.getValue();

		throw new IllegalArgumentException("block does not have attribute of type "+type);
	}

	// UTIL
	@RequiredArgsConstructor
	private static class BlockAttribute<T>
	{

		@Getter
		private final Class<T> type;
		@Getter
		private final T value;

	}

}
