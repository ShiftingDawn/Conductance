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
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_END_STONE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_GRANITE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_NETHERRACK;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_RED_SAND;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_SAND;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_STONE;
import static conductance.api.NCMaterialOreTypes.ORE_TYPE_TUFF;

public final class ConductanceMaterialOreTypes {

	public static void init(MaterialOreTypeRegister register) {
		ORE_TYPE_STONE = register.register("stone", vanillaBlock("stone"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_GRANITE = register.register("granite", vanillaBlock("granite"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_DIORITE = register.register("diorite", vanillaBlock("diorite"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_ANDESITE = register.register("andesite", vanillaBlock("andesite"), false, false, MapColor.STONE, SoundType.STONE);
		ORE_TYPE_TUFF = register.register("tuff", vanillaBlock("tuff"), false, false, MapColor.STONE, SoundType.TUFF);
		ORE_TYPE_DEEPSLATE = register.register("deepslate", vanillaBlock("deepslate"), false, false, MapColor.DEEPSLATE, SoundType.DEEPSLATE);

		ORE_TYPE_NETHERRACK = register.register("netherrack", vanillaBlock("netherrack"), true, false, MapColor.NETHER, SoundType.NETHERRACK);
		ORE_TYPE_BASALT = register.register("basalt", MaterialOreType.OreBlockType.PILLAR, vanillaBlock("basalt"), "basalt", "basalt_%s_ore", true, false, MapColor.COLOR_BLACK, SoundType.BASALT);

		ORE_TYPE_END_STONE = register.register("end_stone", vanillaBlock("end_stone"), true, false, MapColor.SAND, SoundType.STONE);

		ORE_TYPE_SAND = register.register("sand", vanillaBlock("sand"), false, true, MapColor.SAND, SoundType.SAND);
		ORE_TYPE_RED_SAND = register.register("red_sand", vanillaBlock("red_sand"), false, true, MapColor.COLOR_ORANGE, SoundType.SAND);
	}

	private static ResourceLocation vanillaBlock(String name) {
		return ResourceLocation.withDefaultNamespace("block/" + name);
	}
}
