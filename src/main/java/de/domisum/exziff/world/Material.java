package de.domisum.exziff.world;

public enum Material
{
	// @formatter:off
	AIR(0, true),
	STONE(1, false),
	GRASS(2, false),
	DIRT(3, false),
	;
	// @formatter:on


	// PROPERTIES
	public final int id;
	public final boolean transparent;

	// HELPERS
	private static Material[] materialsById;

	static
	{
		int maxId = -1;
		for(Material mat : values())
			if(mat.id > maxId)
				maxId = mat.id;

		materialsById = new Material[maxId+1];

		for(Material material : values())
			materialsById[material.id] = material;
	}


	// INIT
	Material(int id, boolean transparent)
	{
		this.id = id;
		this.transparent = transparent;
	}


	// GETTERS
	public static Material fromId(int id)
	{
		return materialsById[id];
	}

}

