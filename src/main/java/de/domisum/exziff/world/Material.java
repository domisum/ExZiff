package de.domisum.exziff.world;

public enum Material
{
	// @formatter:off
	AIR(0, 0, true),

	STONE(1, 0, false),
	GRANITE(1,1, false),
	DIORITE(1, 3, false),
	ANDESITE(1, 5, false),

	GRASS(2, 0, false),
	DIRT(3, 0, false),
	COBBLESTONE(4, 0, false),

	PLANK_OAK(5, 0, false),
	PLANK_SPRUCE(5, 1, false),
	PLANK_BIRCH(5, 2, false),
	PLANK_JUNGLE(5, 3, false),
	PLANK_ACACIA(5, 4, false),
	PLANK_DARK_OAK(5, 5, false),

	WATER(9, 0, false),
	;
	// @formatter:on


	// PROPERTIES
	public final int id;
	public final int subId;
	public final boolean transparent;

	// HELPERS
	private static Material[][] materialsById;

	static
	{
		int maxId = -1;
		for(Material mat : values())
			if(mat.id > maxId)
				maxId = mat.id;

		materialsById = new Material[16][maxId+1];

		for(Material material : values())
			materialsById[material.id][material.subId] = material;
	}


	// INIT
	Material(int id, int subId, boolean transparent)
	{
		this.id = id;
		this.subId = subId;
		this.transparent = transparent;
	}


	// GETTERS
	public static Material fromId(int id, int subId)
	{
		return materialsById[id][subId];
	}

}

