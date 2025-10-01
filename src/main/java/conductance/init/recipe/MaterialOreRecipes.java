package conductance.init.recipe;

import java.util.function.BiConsumer;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.api.NCRecipeTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialTraitOre;
import conductance.api.recipe.event.RegisterRecipeEvent;
import static conductance.api.CAPI.materials;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.INGOT;
import static conductance.api.NCMaterialGenerationHandlers.RAW_ORE;
import static conductance.api.NCMaterialGenerationHandlers.RAW_ORE_BLOCK;

final class MaterialOreRecipes {

	public static void add(final RegisterRecipeEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			if (material.hasTrait(NCMaterialTraits.ORE)) {
				MaterialOreRecipes.addOreRecipes(event, material, material.getTrait(NCMaterialTraits.ORE));
			}
		}
	}

	private static void addOreRecipes(final RegisterRecipeEvent event, final Material material, final MaterialTraitOre trait) {
		if (!materials().hasItemOverride(material, RAW_ORE_BLOCK)) {
			event.shapeless("raw_%s_block".formatted(material.getName()), RAW_ORE_BLOCK, material,
				b -> b.add(RAW_ORE, material, 9));
		}
		if (!materials().hasItemOverride(material, RAW_ORE)) {
			event.shapeless("raw_%s_from_block".formatted(material.getName()), RAW_ORE, material, 9,
				b -> b.add(RAW_ORE_BLOCK, material));
		}
		final Material pulverizeInto = trait.getPulverizeResult() != null ? trait.getPulverizeResult().get() : material;
		final Material smeltInto = trait.getSmeltResult() != null ? trait.getSmeltResult().get() : material;
		final MaterialGenerationHandler smeltType = INGOT.test(material) ? INGOT : GEM.test(material) ? GEM : DUST;
		final BiConsumer<MaterialGenerationHandler, Integer> smeltMaker = (handler, multiplier) ->
			event.smelting("%s_from_%s".formatted(smeltType.getUnlocalizedName(smeltInto), handler.getUnlocalizedName(material)), smeltType, smeltInto, multiplier,
				b -> b.ingredient(materials().getItem(material, handler)).experience(0.3f * multiplier));
		final BiConsumer<MaterialGenerationHandler, Integer> blastMaker = (handler, multiplier) ->
			event.blasting("%s_from_%s".formatted(smeltType.getUnlocalizedName(smeltInto), handler.getUnlocalizedName(material)), smeltType, smeltInto, multiplier,
				b -> b.ingredient(materials().getItem(material, handler)).experience(0.3f * multiplier));

		CAPI.regs().materialGenerationHandlers().stream().filter(handler -> handler.getOreBearer() != null).forEach(handler -> {
			final int multiplier = (handler.getOreBearer().hasDoubleOutput() ? 2 : 1) * trait.getDropMultiplier();
			if (!CAPI.materials().hasItemOverride(smeltInto, smeltType) || !CAPI.materials().hasItemOverride(material, handler)) {
				smeltMaker.accept(handler, multiplier);
				blastMaker.accept(handler, multiplier);
			}
			event.create("%s_dust_from_%s".formatted(pulverizeInto.getName(), handler.getUnlocalizedName(material)), NCRecipeTypes.PULVERIZER,
				b -> b.in(CAPI.materials().getItem(material, handler)).out(CAPI.materials().getItem(pulverizeInto, DUST, 2 * multiplier)).duration(200));
		});
		//Double output for raw ore is handled by the ore block drop
		smeltMaker.accept(RAW_ORE, trait.getDropMultiplier());
		blastMaker.accept(RAW_ORE, trait.getDropMultiplier());
		event.create("%s_dust_from_raw_%s".formatted(pulverizeInto.getName(), material.getName()), NCRecipeTypes.PULVERIZER,
			b -> b.in(material, RAW_ORE).out(CAPI.materials().getItem(pulverizeInto, DUST, 2 * trait.getDropMultiplier())).duration(200));
		smeltMaker.accept(RAW_ORE_BLOCK, 9 * trait.getDropMultiplier());
		blastMaker.accept(RAW_ORE_BLOCK, 9 * trait.getDropMultiplier());
		event.create("%s_dust_from_raw_%s_block".formatted(pulverizeInto.getName(), material.getName()), NCRecipeTypes.PULVERIZER,
			b -> b.in(material, RAW_ORE_BLOCK).out(CAPI.materials().getItem(pulverizeInto, DUST, 18 * trait.getDropMultiplier())).duration(200));
	}

	private MaterialOreRecipes() {
	}
}
