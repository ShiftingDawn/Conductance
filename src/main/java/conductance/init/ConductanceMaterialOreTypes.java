package conductance.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.material.MaterialOreType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterMaterialOreTypeEvent;
import conductance.Conductance;
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

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceMaterialOreTypes {

	@EventListener(priority = -100)
	public static void init(final RegisterMaterialOreTypeEvent event) {
		ORE_TYPE_STONE = event.register("stone", ConductanceMaterialOreTypes.vanillaBlock("stone"), MapColor.STONE, SoundType.STONE);
		ORE_TYPE_GRANITE = event.register("granite", ConductanceMaterialOreTypes.vanillaBlock("granite"), MapColor.STONE, SoundType.STONE);
		ORE_TYPE_DIORITE = event.register("diorite", ConductanceMaterialOreTypes.vanillaBlock("diorite"), MapColor.STONE, SoundType.STONE);
		ORE_TYPE_ANDESITE = event.register("andesite", ConductanceMaterialOreTypes.vanillaBlock("andesite"), MapColor.STONE, SoundType.STONE);
		ORE_TYPE_TUFF = event.register("tuff", ConductanceMaterialOreTypes.vanillaBlock("tuff"), MapColor.STONE, SoundType.TUFF);
		ORE_TYPE_DEEPSLATE = event.register("deepslate", ConductanceMaterialOreTypes.vanillaBlock("deepslate"), MapColor.DEEPSLATE, SoundType.DEEPSLATE);

		ORE_TYPE_NETHERRACK = event.register("netherrack", ConductanceMaterialOreTypes.vanillaBlock("netherrack"), MapColor.NETHER, SoundType.NETHERRACK,
				RegisterMaterialOreTypeEvent.MaterialOreTypeBuilder::doubleOutput);
		ORE_TYPE_BASALT = event.register("basalt", ConductanceMaterialOreTypes.vanillaBlock("basalt"), MapColor.COLOR_BLACK, SoundType.BASALT,
				builder -> builder.blockType(MaterialOreType.OreBlockType.PILLAR).doubleOutput());
		ORE_TYPE_BLACKSTONE = event.register("blackstone", ConductanceMaterialOreTypes.vanillaBlock("blackstone"), MapColor.COLOR_BLACK, SoundType.STONE,
				RegisterMaterialOreTypeEvent.MaterialOreTypeBuilder::doubleOutput);

		ORE_TYPE_END_STONE = event.register("end_stone", ConductanceMaterialOreTypes.vanillaBlock("end_stone"), MapColor.SAND, SoundType.STONE, RegisterMaterialOreTypeEvent.MaterialOreTypeBuilder::doubleOutput);

		ORE_TYPE_GRAVEL = event.register("gravel", ConductanceMaterialOreTypes.vanillaBlock("gravel"), MapColor.STONE, SoundType.GRAVEL,
				RegisterMaterialOreTypeEvent.MaterialOreTypeBuilder::hasGravity);
		ORE_TYPE_SAND = event.register("sand", ConductanceMaterialOreTypes.vanillaBlock("sand"), MapColor.SAND, SoundType.SAND,
				RegisterMaterialOreTypeEvent.MaterialOreTypeBuilder::hasGravity);
		ORE_TYPE_RED_SAND = event.register("red_sand", ConductanceMaterialOreTypes.vanillaBlock("red_sand"), MapColor.COLOR_ORANGE, SoundType.SAND,
				RegisterMaterialOreTypeEvent.MaterialOreTypeBuilder::hasGravity);
	}

	private static ResourceLocation vanillaBlock(final String name) {
		return ResourceLocation.withDefaultNamespace("block/" + name);
	}

	private ConductanceMaterialOreTypes() {
	}
}
