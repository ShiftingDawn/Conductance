package conductance.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.material.MaterialOreType;
import conductance.api.plugin.MaterialOreTypeRegister;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_ANDESITE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_BASALT;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_DEEPSLATE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_DIORITE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_GRANITE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_NETHERRACK;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_RED_SAND;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_SAND;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_STONE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_TUFF;

public final class ConductanceMaterialOreTypes {

	public static void init(final MaterialOreTypeRegister register) {
		ORE_TYPE_STONE = register.register("stone", ConductanceMaterialOreTypes.vanillaBlock("stone"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_GRANITE = register.register("granite", ConductanceMaterialOreTypes.vanillaBlock("granite"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_DIORITE = register.register("diorite", ConductanceMaterialOreTypes.vanillaBlock("diorite"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_ANDESITE = register.register("andesite", ConductanceMaterialOreTypes.vanillaBlock("andesite"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_TUFF = register.register("tuff", ConductanceMaterialOreTypes.vanillaBlock("tuff"), false, false, MapColor.STONE, SoundType.TUFF);
		ORE_TYPE_DEEPSLATE = register.register("deepslate", ConductanceMaterialOreTypes.vanillaBlock("deepslate"), false, false, MapColor.DEEPSLATE, SoundType.DEEPSLATE);

		ORE_TYPE_NETHERRACK = register.register("netherrack", ConductanceMaterialOreTypes.vanillaBlock("netherrack"), true, false, MapColor.NETHER, SoundType.NETHERRACK);
		ORE_TYPE_BASALT = register.register("basalt", MaterialOreType.OreBlockType.PILLAR, ConductanceMaterialOreTypes.vanillaBlock("basalt"), "basalt", "basalt_%s_ore", true, false, MapColor.COLOR_BLACK,
				SoundType.BASALT);

		ORE_TYPE_SAND = register.register("sand", ConductanceMaterialOreTypes.vanillaBlock("sand"), false, true, MapColor.SAND, SoundType.SAND);
		ORE_TYPE_RED_SAND = register.register("red_sand", ConductanceMaterialOreTypes.vanillaBlock("red_sand"), false, true, MapColor.COLOR_ORANGE, SoundType.SAND);
	}

	private static ResourceLocation vanillaBlock(final String name) {
		return ResourceLocation.withDefaultNamespace("block/" + name);
	}

	private ConductanceMaterialOreTypes() {
	}
}
