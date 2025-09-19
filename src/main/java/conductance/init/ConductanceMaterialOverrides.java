package conductance.init;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.NCMaterials;
import conductance.api.material.event.RegisterMaterialOverridesEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMaterialOverrides {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialOverridesEvent event) {
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.INGOT, Items.IRON_INGOT);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.NUGGET, Items.IRON_NUGGET);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.STORAGE_BLOCK, Blocks.IRON_BLOCK);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.RAW_ORE, Items.RAW_IRON);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.RAW_ORE_BLOCK, Items.RAW_IRON_BLOCK);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.ORE_STONE, Items.IRON_ORE);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.ORE_DEEPSLATE, Items.DEEPSLATE_IRON_ORE);

		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.INGOT, Items.COPPER_INGOT);
		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.STORAGE_BLOCK, Blocks.COPPER_BLOCK);
		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.RAW_ORE, Items.RAW_COPPER);
		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.RAW_ORE_BLOCK, Items.RAW_COPPER_BLOCK);
		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.ORE_STONE, Items.COPPER_ORE);
		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.ORE_DEEPSLATE, Items.DEEPSLATE_COPPER_ORE);

		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.INGOT, Items.GOLD_INGOT);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.NUGGET, Items.GOLD_NUGGET);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.STORAGE_BLOCK, Blocks.GOLD_BLOCK);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.RAW_ORE, Items.RAW_GOLD);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.RAW_ORE_BLOCK, Items.RAW_GOLD_BLOCK);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.ORE_STONE, Items.GOLD_ORE);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.ORE_DEEPSLATE, Items.DEEPSLATE_GOLD_ORE);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.ORE_NETHERRACK, Items.NETHER_GOLD_ORE);
	}

	private ConductanceMaterialOverrides() {
	}
}
