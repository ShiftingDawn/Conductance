package conductance.init.recipe;

import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.material.Material;
import conductance.api.recipe.event.RegisterRecipeEvent;
import static conductance.api.CAPI.materials;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.INGOT;
import static conductance.api.NCMaterialGenerationHandlers.NUGGET;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.PLATE_DOUBLE;
import static conductance.api.NCMaterialGenerationHandlers.STORAGE_BLOCK;

final class MaterialDynamicRecipeHandler {

	public static void add(final RegisterRecipeEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			MaterialDynamicRecipeHandler.addAllRecipes(event, material);
		}
	}

	private static void addAllRecipes(final RegisterRecipeEvent event, final Material material) {
		if (STORAGE_BLOCK.test(material)) {
			if (!materials().hasItemOverride(material, STORAGE_BLOCK)) {
				if (material.hasFlag(NCMaterialFlags.INGOT)) {
					event.shapeless("%s_block".formatted(material.getName()), materials().getItem(material, STORAGE_BLOCK),
							b -> b.add(materials().getItemTag(material, INGOT), 9));
					event.shapeless("%s_ingot_from_block".formatted(material.getName()), materials().getItem(material, INGOT, 9),
							b -> b.add(materials().getItemTag(material, STORAGE_BLOCK)));
				} else if (material.hasFlag(NCMaterialFlags.GEM)) {
					event.shapeless("%s_block".formatted(material.getName()), materials().getItem(material, STORAGE_BLOCK),
							b -> b.add(materials().getItemTag(material, GEM), 9));
					event.shapeless("%s_ingot_from_block".formatted(material.getName()), materials().getItem(material, GEM, 9),
							b -> b.add(materials().getItemTag(material, STORAGE_BLOCK)));
				}
			}
		}
		if (NUGGET.test(material)) {
			if (!materials().hasItemOverride(material, NUGGET)) {
				if (material.hasFlag(NCMaterialFlags.INGOT)) {
					event.shapeless("%s_nugget".formatted(material.getName()), materials().getItem(material, NUGGET, 9),
							b -> b.add(materials().getItemTag(material, INGOT)));
					event.shapeless("%s_ingot_from_nuggets".formatted(material.getName()), materials().getItem(material, INGOT),
							b -> b.add(materials().getItemTag(material, NUGGET), 9));
				}
			}
		}
		if (PLATE.test(material)) {
			if (material.hasFlag(NCMaterialFlags.INGOT)) {
				event.shaped("%s_plate".formatted(material.getName()), materials().getItem(material, PLATE),
						b -> b.pattern("H", "a", "a").key('a', materials().getItemTag(material, INGOT)));
				event.shaped("double_%s_plate".formatted(material.getName()), materials().getItem(material, PLATE_DOUBLE),
						b -> b.pattern("H", "a", "a").key('a', materials().getItemTag(material, PLATE)));
			}
			//Gem <-> plate will be handles by cutting machine later
			//Dust <-> plate will be handles by compressor machine later
		}
	}

	private MaterialDynamicRecipeHandler() {
	}
}
