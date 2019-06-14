package de.domisum.exziff.bedrockregion.blockstack;

import de.domisum.exziff.oldworld.Material;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BlockStackSimpleTest
{

	@Test public void bothAreSameMerge()
	{
		assertMaterialAt(new BlockStackSimple(new Material[] {Material.STONE, Material.STONE, Material.STONE}), 2,
				Material.STONE);
		assertMaterialAt(new BlockStackSimple(new Material[] {Material.STONE, Material.STONE, Material.STONE}), 3, Material.AIR);

		assertBlockStackMaxY(new BlockStackSimple(new Material[] {Material.STONE, Material.STONE, Material.STONE}), 2);
	}


	private static void assertBlockStackMaxY(BlockStack bs, int maxY)
	{
		Assertions.assertEquals(maxY, bs.getMaximumY(), "wrong maximumY");
	}

	private static void assertMaterialAt(BlockStack bs, int y, Material material)
	{
		Assertions.assertEquals(material, bs.getMaterialAt(y), "wrong material at y"+y);
	}

}
