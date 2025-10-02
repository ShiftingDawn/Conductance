package conductance.init.recipe;

import conductance.api.CAPI;
import conductance.api.NCRecipeTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.recipe.event.RegisterRecipeEvent;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.LIQUID;
import static conductance.api.recipe.RecipeHelper.calc;

final class MaterialRecycleRecipes {

	public static void add(final RegisterRecipeEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			if (DUST.test(material)) {
				MaterialRecycleRecipes.addDustRecipes(event, material);
			}
			if (LIQUID.test(material)) {
				MaterialRecycleRecipes.addLiquidRecipes(event, material);
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

	private static void addLiquidRecipes(final RegisterRecipeEvent event, final Material material) {
		for (final MaterialGenerationHandler handler : CAPI.regs().materialGenerationHandlers()) {
			if (!handler.test(material)) {
				continue;
			}
			if ((handler.hasItem() || handler.hasBlock()) && handler.getUnitValue(material) > 0) {
				final int amount = (int) (((float) handler.getUnitValue(material) / (float) CAPI.UNIT) * 144);
				event.create("recycling/%s_from_%s".formatted(LIQUID.getUnlocalizedName(material), handler.getId().getPath()), NCRecipeTypes.EXTRACTOR,
					b -> b.in(material, handler).out(material, LIQUID, amount).duration((int) material.getMass()));
			}
		}
	}

	private MaterialRecycleRecipes() {
	}
}
