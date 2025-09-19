package conductance.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.event.MaterialOreBearerBuilder;
import conductance.api.material.event.RegisterMaterialOreBearerEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMaterialOreBearers.ANDESITE;
import static conductance.api.NCMaterialOreBearers.BASALT;
import static conductance.api.NCMaterialOreBearers.BLACKSTONE;
import static conductance.api.NCMaterialOreBearers.DEEPSLATE;
import static conductance.api.NCMaterialOreBearers.DIORITE;
import static conductance.api.NCMaterialOreBearers.END_STONE;
import static conductance.api.NCMaterialOreBearers.GRANITE;
import static conductance.api.NCMaterialOreBearers.GRAVEL;
import static conductance.api.NCMaterialOreBearers.NETHERRACK;
import static conductance.api.NCMaterialOreBearers.RED_SAND;
import static conductance.api.NCMaterialOreBearers.SAND;
import static conductance.api.NCMaterialOreBearers.STONE;
import static conductance.api.NCMaterialOreBearers.TUFF;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialOreBearers {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialOreBearerEvent event) {
		STONE = event.register("stone", ConductanceMaterialOreBearers.vanillaBlock("stone"), MapColor.STONE, SoundType.STONE,
			null);
		GRANITE = event.register("granite", ConductanceMaterialOreBearers.vanillaBlock("granite"), MapColor.STONE, SoundType.STONE,
			null);
		DIORITE = event.register("diorite", ConductanceMaterialOreBearers.vanillaBlock("diorite"), MapColor.STONE, SoundType.STONE,
			null);
		ANDESITE = event.register("andesite", ConductanceMaterialOreBearers.vanillaBlock("andesite"), MapColor.STONE, SoundType.STONE,
			null);
		TUFF = event.register("tuff", ConductanceMaterialOreBearers.vanillaBlock("tuff"), MapColor.STONE, SoundType.TUFF,
			null);
		DEEPSLATE = event.register("deepslate", ConductanceMaterialOreBearers.vanillaBlock("deepslate"), MapColor.DEEPSLATE, SoundType.DEEPSLATE,
			null);

		NETHERRACK = event.register("netherrack", ConductanceMaterialOreBearers.vanillaBlock("netherrack"), MapColor.NETHER, SoundType.NETHERRACK,
			MaterialOreBearerBuilder::doubleOutput);
		BASALT = event.register("basalt", ConductanceMaterialOreBearers.vanillaBlock("basalt"), MapColor.COLOR_BLACK, SoundType.BASALT,
			builder -> builder.blockType(MaterialOreBearer.BlockType.PILLAR).doubleOutput());
		BLACKSTONE = event.register("blackstone", ConductanceMaterialOreBearers.vanillaBlock("blackstone"), MapColor.COLOR_BLACK, SoundType.STONE,
			MaterialOreBearerBuilder::doubleOutput);

		END_STONE = event.register("end_stone", ConductanceMaterialOreBearers.vanillaBlock("end_stone"), MapColor.SAND, SoundType.STONE,
			MaterialOreBearerBuilder::doubleOutput);

		GRAVEL = event.register("gravel", ConductanceMaterialOreBearers.vanillaBlock("gravel"), MapColor.STONE, SoundType.GRAVEL,
			MaterialOreBearerBuilder::hasGravity);
		SAND = event.register("sand", ConductanceMaterialOreBearers.vanillaBlock("sand"), MapColor.SAND, SoundType.SAND,
			MaterialOreBearerBuilder::hasGravity);
		RED_SAND = event.register("red_sand", ConductanceMaterialOreBearers.vanillaBlock("red_sand"), MapColor.COLOR_ORANGE, SoundType.SAND,
			MaterialOreBearerBuilder::hasGravity);
	}

	private static ResourceLocation vanillaBlock(final String name) {
		return ResourceLocation.withDefaultNamespace("block/" + name);
	}

	private ConductanceMaterialOreBearers() {
	}
}
