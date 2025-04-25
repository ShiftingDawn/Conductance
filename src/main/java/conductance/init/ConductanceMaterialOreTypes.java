package conductance.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.material.MaterialOreType;
import conductance.api.plugin.MaterialOreTypeRegister;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_ANDESITE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_BASALT;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_BLACKSTONE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_DEEPSLATE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_DIORITE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_END_STONE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_GRANITE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_GRAVEL;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_NETHERRACK;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_RED_SAND;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_SAND;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_STONE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_TUFF;

public final class ConductanceMaterialOreTypes {

	public static void init(final MaterialOreTypeRegister register) {
		ORE_TYPE_STONE = register.register("stone", ConductanceMaterialOreTypes.vanillaBlock("stone"), MapColor.STONE, SoundType.STONE)
				.build();
		ORE_TYPE_GRANITE = register.register("granite", ConductanceMaterialOreTypes.vanillaBlock("granite"), MapColor.STONE, SoundType.STONE)
				.build();
		ORE_TYPE_DIORITE = register.register("diorite", ConductanceMaterialOreTypes.vanillaBlock("diorite"), MapColor.STONE, SoundType.STONE)
				.build();
		ORE_TYPE_ANDESITE = register.register("andesite", ConductanceMaterialOreTypes.vanillaBlock("andesite"), MapColor.STONE, SoundType.STONE)
				.build();
		ORE_TYPE_TUFF = register.register("tuff", ConductanceMaterialOreTypes.vanillaBlock("tuff"), MapColor.STONE, SoundType.TUFF)
				.build();
		ORE_TYPE_DEEPSLATE = register.register("deepslate", ConductanceMaterialOreTypes.vanillaBlock("deepslate"), MapColor.DEEPSLATE, SoundType.DEEPSLATE)
				.build();

		ORE_TYPE_NETHERRACK = register.register("netherrack", ConductanceMaterialOreTypes.vanillaBlock("netherrack"), MapColor.NETHER, SoundType.NETHERRACK)
				.doubleOutput()
				.build();
		ORE_TYPE_BASALT = register.register("basalt", ConductanceMaterialOreTypes.vanillaBlock("basalt"), MapColor.COLOR_BLACK, SoundType.BASALT)
				.blockType(MaterialOreType.OreBlockType.PILLAR)
				.doubleOutput()
				.build();
		ORE_TYPE_BLACKSTONE = register.register("blackstone", ConductanceMaterialOreTypes.vanillaBlock("blackstone"), MapColor.COLOR_BLACK, SoundType.STONE)
				.doubleOutput()
				.build();

		ORE_TYPE_END_STONE = register.register("end_stone", ConductanceMaterialOreTypes.vanillaBlock("end_stone"), MapColor.SAND, SoundType.STONE)
				.doubleOutput()
				.build();

		ORE_TYPE_GRAVEL = register.register("gravel", ConductanceMaterialOreTypes.vanillaBlock("gravel"), MapColor.STONE, SoundType.GRAVEL)
				.hasGravity()
				.build();
		ORE_TYPE_SAND = register.register("sand", ConductanceMaterialOreTypes.vanillaBlock("sand"), MapColor.SAND, SoundType.SAND)
				.hasGravity()
				.build();
		ORE_TYPE_RED_SAND = register.register("red_sand", ConductanceMaterialOreTypes.vanillaBlock("red_sand"), MapColor.COLOR_ORANGE, SoundType.SAND)
				.hasGravity()
				.build();
	}

	private static ResourceLocation vanillaBlock(final String name) {
		return ResourceLocation.withDefaultNamespace("block/" + name);
	}

	private ConductanceMaterialOreTypes() {
	}
}
