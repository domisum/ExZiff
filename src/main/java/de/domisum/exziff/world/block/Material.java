package de.domisum.exziff.world.block;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public enum Material
{

	STONE(),
	GRANITE(),
	GRANITE_POLISHED(),
	DIORITE(),
	DIORITE_POLISHED(),
	ANDESITE(),
	ANDESITE_POLISHED(),
	COBBLESTONE(),

	GRASS(),
	DIRT(),
	DIRT_COARSE(),
	DIRT_PODZOL(),

	LOG_OAK(OptionalAxis.class),
	LOG_SPRUCE(OptionalAxis.class),
	LOG_BIRCH(OptionalAxis.class),
	LOG_JUNGLE(OptionalAxis.class),
	LOG_ACACIA(OptionalAxis.class),
	LOG_DARK_OAK(OptionalAxis.class),

	SLAB_OAK(HorizontalOrientation.class),
	SLAB_SPRUCE(HorizontalOrientation.class),

	// vertical orientation: from low side to high side
	STAIR_OAK(HorizontalOrientation.class, VerticalOrientation.class),
	STAIR_COBBLESTONE(HorizontalOrientation.class, VerticalOrientation.class),
	;

	public final Set<Class<?>> blockAttributes;


	// INIT
	Material(Class<?>... classes)
	{
		this(new HashSet<>(Arrays.asList(classes)));
	}

}
