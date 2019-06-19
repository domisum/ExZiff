package de.domisum.exziff.world.block;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public enum Material
{

	AIR(),

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

	LOG_OAK(Axis.class),
	LOG_SPRUCE(Axis.class),
	LOG_BIRCH(Axis.class),
	LOG_JUNGLE(Axis.class),
	LOG_ACACIA(Axis.class),
	LOG_DARK_OAK(Axis.class),
	LEAVES_OAK(),
	LEAVES_SPRUCE(),
	LEAVES_BIRCH(),
	LEAVES_JUNGLE(),
	LEAVES_ACACIA(),
	LEAVES_DARK_OAK(),

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

	public static Material ofId(int ordinal)
	{
		return values()[ordinal];
	}

}
