package conductance.init.material;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTextureSets;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.RegisterTagEvent;
import conductance.Conductance;
import static net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL;
import static conductance.api.NCMaterialTextureSets.FINE;
import static conductance.api.NCMaterialTextureSets.METALLIC;
import static conductance.api.NCMaterialTextureSets.ROUGH;
import static conductance.api.NCMaterialTextureSets.SAND;
import static conductance.api.NCMaterialTextureSets.SHINY;
import static conductance.api.NCMaterials.ALMANDINE;
import static conductance.api.NCMaterials.ALUMINIUM;
import static conductance.api.NCMaterials.AMETHYST;
import static conductance.api.NCMaterials.ANDRADITE;
import static conductance.api.NCMaterials.BIOTITE;
import static conductance.api.NCMaterials.BLAZE;
import static conductance.api.NCMaterials.CALCIUM;
import static conductance.api.NCMaterials.CHROMIUM;
import static conductance.api.NCMaterials.CLAY;
import static conductance.api.NCMaterials.COBALT_BRASS;
import static conductance.api.NCMaterials.DEEPSLATE;
import static conductance.api.NCMaterials.ENDER_PEARL;
import static conductance.api.NCMaterials.EYE_OF_ENDER;
import static conductance.api.NCMaterials.FLINT;
import static conductance.api.NCMaterials.GARNET_SAND;
import static conductance.api.NCMaterials.GROSSULAR;
import static conductance.api.NCMaterials.IRON;
import static conductance.api.NCMaterials.LAPIS_LAZULI;
import static conductance.api.NCMaterials.LAZURITE;
import static conductance.api.NCMaterials.LITHIUM;
import static conductance.api.NCMaterials.OXYGEN;
import static conductance.api.NCMaterials.PYRITE;
import static conductance.api.NCMaterials.PYROPE;
import static conductance.api.NCMaterials.RUBY;
import static conductance.api.NCMaterials.SILICON;
import static conductance.api.NCMaterials.SILICON_DIOXIDE;
import static conductance.api.NCMaterials.SODALITE;
import static conductance.api.NCMaterials.SODIUM;
import static conductance.api.NCMaterials.SPESSARTINE;
import static conductance.api.NCMaterials.STEEL;
import static conductance.api.NCMaterials.SULFUR;
import static conductance.api.NCMaterials.TUNGSTEN;
import static conductance.api.NCMaterials.TUNGSTENSTEEL;
import static conductance.api.NCMaterials.UVAROVITE;
import static conductance.api.NCMaterials.WATER;

@ConductancePluginListener(modid = Conductance.MODID)
public final class MaterialLoader2 {

	@EventListener(priority = -96)
	private static void initialize(final RegisterMaterialEvent event) {
		TUNGSTENSTEEL = event.register("tungstensteel", b -> b
			.metalAll().fineWire()
			.liquid(2011)
			.style(0x6464A0, METALLIC)
			.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_DIAMOND_TOOL)
			.components(TUNGSTEN, STEEL)
		);
		RUBY = event.register("ruby", b -> b
			.gemExtra()
			.ore()
			.style(0xFF6464, NCMaterialTextureSets.AMETHYST)
			.components(CHROMIUM, ALUMINIUM, 2, OXYGEN, 3)
		);
		FLINT = event.register("flint", b -> b
			.dust().gem(true, false, false)
			.style(0x002040, NCMaterialTextureSets.FLINT)
			.components(SILICON_DIOXIDE)
		);
		CLAY = event.register("clay", b -> b
			.dust()
			.style(0xC8C8DC, ROUGH)
			.components(SODIUM, 2, LITHIUM, ALUMINIUM, 2, SILICON, 2, WATER, 6)
		);
		GARNET_SAND = event.register("garnet_sand", b -> b
			.dust()
			.ore()
			.style(0xC86400, SAND)
			.components(ALMANDINE, ANDRADITE, GROSSULAR, PYROPE, SPESSARTINE, UVAROVITE)
		);
		LAPIS_LAZULI = event.register("lapis_lazuli", b -> b
			.gemExtra()
			.removeFlag(NCMaterialFlags.GEM_FLAWED, NCMaterialFlags.GEM_FLAWLESS, NCMaterialFlags.GEM_EXQUISITE)
			.ore(6, 4)
			.style(0x4646DC, NCMaterialTextureSets.LAPIS)
			.components(LAZURITE, 12, SODALITE, 2, PYRITE, CALCIUM)
		);
		COBALT_BRASS = event.register("cobalt_brass", b -> b
			.metalExtra()
			.liquid(1202)
			.style(0xB4B4A0, METALLIC)
		);
		BLAZE = event.register("blaze", b -> b
			.dust()
			.liquid(4000)
			.style(0xFFC800, FINE)
			.components(SULFUR)
		);
		EYE_OF_ENDER = event.register("eye_of_ender", b -> b
			.gemExtra()
			.removeFlag(NCMaterialFlags.GEM_FLAWED, NCMaterialFlags.GEM_FLAWLESS, NCMaterialFlags.GEM_EXQUISITE)
			.color(160, 250, 230).textureSet(SHINY)
			.components(ENDER_PEARL, BLAZE)
		);
		AMETHYST = event.register("amethyst", b -> b
			.gemExtra()
			.ore()
			.style(0x734FBC, NCMaterialTextureSets.AMETHYST)
			.prop(NCMaterialProps.REQUIRED_TOOL_LEVEL, NEEDS_IRON_TOOL)
			.components(SILICON_DIOXIDE, 4, IRON)
		);
		DEEPSLATE = event.register("deepslate", b -> b
			.dust()
			.style(0x2F2F37, ROUGH)
			.components(SILICON_DIOXIDE, 4, BIOTITE)
		);
	}

	@EventListener(priority = -100)
	private static void addCustomTags(final RegisterTagEvent event) {
		event.item(CAPI.materials().getItemTag(FLINT, NCMaterialGenerationHandlers.GEM), Items.FLINT);
		event.tag(CAPI.materials().getItemTag(LAPIS_LAZULI, NCMaterialGenerationHandlers.GEM), Tags.Items.GEMS_LAPIS.location());
		event.tag(CAPI.materials().getItemTag(LAPIS_LAZULI, NCMaterialGenerationHandlers.STORAGE_BLOCK), Tags.Items.STORAGE_BLOCKS_LAPIS.location());
		event.item(CAPI.materials().getItemTag(AMETHYST, NCMaterialGenerationHandlers.STORAGE_BLOCK), Blocks.AMETHYST_BLOCK);
	}

	private MaterialLoader2() {
	}
}
