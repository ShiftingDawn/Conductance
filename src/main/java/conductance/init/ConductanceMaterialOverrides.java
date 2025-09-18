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
public final class ConductanceMaterialOverrides {

	@EventListener(priority = -100)
	private static void init(final RegisterMaterialOverridesEvent event) {
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.INGOT, Items.IRON_INGOT);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.NUGGET, Items.IRON_NUGGET);
		event.add(NCMaterials.IRON, NCMaterialGenerationHandlers.STORAGE_BLOCK, Blocks.IRON_BLOCK);

		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.INGOT, Items.COPPER_INGOT);
		event.add(NCMaterials.COPPER, NCMaterialGenerationHandlers.STORAGE_BLOCK, Blocks.COPPER_BLOCK);

		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.INGOT, Items.GOLD_INGOT);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.NUGGET, Items.GOLD_NUGGET);
		event.add(NCMaterials.GOLD, NCMaterialGenerationHandlers.STORAGE_BLOCK, Blocks.GOLD_BLOCK);
	}

	private ConductanceMaterialOverrides() {
	}
}
