package conductance.init.recipe;

import conductance.api.CAPI;
import conductance.api.NCRecipeTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.recipe.event.RegisterRecipeEvent;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.recipe.RecipeHelper.calc;

final class MaterialRecycleRecipes {

	public static void add(final RegisterRecipeEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			if (DUST.test(material)) {
				MaterialRecycleRecipes.addDustRecipes(event, material);
			}
		}
	}

	private static void addDustRecipes(final RegisterRecipeEvent event, final Material material) {
		for (final MaterialGenerationHandler handler : CAPI.regs().materialGenerationHandlers()) {
			if (handler == DUST || !handler.test(material)) {
				continue;
			}
			if ((handler.hasItem() || handler.hasBlock()) && handler.getUnitValue(material) >= DUST.getUnitValue(material)) {
				calc(material, handler, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("recycling/%s_dust_from_%s".formatted(material.getName(), handler.getId().getPath()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, handler, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
	}

	private MaterialRecycleRecipes() {
	}
}
