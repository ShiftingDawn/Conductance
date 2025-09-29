package conductance.init.material;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import conductance.api.CAPI;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.RegisterTagEvent;
import conductance.Conductance;
import static conductance.api.NCMaterialTextureSets.METALLIC;
import static conductance.api.NCMaterialTextureSets.ROUGH;
import static conductance.api.NCMaterials.BRICK;
import static conductance.api.NCMaterials.CHROMIUM;
import static conductance.api.NCMaterials.CLAY;
import static conductance.api.NCMaterials.DIATOMITE;
import static conductance.api.NCMaterials.FLINT;
import static conductance.api.NCMaterials.HEMATITE;
import static conductance.api.NCMaterials.HSS_G;
import static conductance.api.NCMaterials.MERCURY;
import static conductance.api.NCMaterials.MOLYBDENUM;
import static conductance.api.NCMaterials.PYRITE;
import static conductance.api.NCMaterials.REDSTONE;
import static conductance.api.NCMaterials.RUBY;
import static conductance.api.NCMaterials.SAPPHIRE;
import static conductance.api.NCMaterials.SILICON;
import static conductance.api.NCMaterials.TUNGSTENSTEEL;
import static conductance.api.NCMaterials.VANADIUM;

@ConductancePluginListener(modid = Conductance.MODID)
public final class MaterialLoader3 {

	@EventListener(priority = -94)
	private static void initialize(final RegisterMaterialEvent event) {
		REDSTONE = event.register("redstone", b -> b
			.dust().plate()
			.ore(5, 1, true)
			.liquid(500)
			.style(0xC80000, ROUGH)
			.components(SILICON, PYRITE, 5, RUBY, MERCURY)
		);
		BRICK = event.register("brick", b -> b
			.dust().ingot(true, false)
			.style(0x9B5643, ROUGH)
			.components(CLAY, 1)
		);
		DIATOMITE = event.register("diatomite", b -> b
			.dust()
			.ore()
			.color(0x19E1E1)
			.components(FLINT, 8, HEMATITE, SAPPHIRE)
		);
		HSS_G = event.register("hss_g", b -> b
			.metalExtra().fineWire()
			.style(0x999900, METALLIC)
			.components(TUNGSTENSTEEL, 5, CHROMIUM, MOLYBDENUM, 2, VANADIUM)
		);
	}

	@EventListener(priority = -100)
	private static void addCustomTags(final RegisterTagEvent event) {
		event.item(CAPI.materials().getItemTag(BRICK, NCMaterialGenerationHandlers.INGOT), Items.BRICK);
		event.item(CAPI.materials().getItemTag(BRICK, NCMaterialGenerationHandlers.STORAGE_BLOCK), Blocks.BRICKS);
	}

	private MaterialLoader3() {
	}
}
