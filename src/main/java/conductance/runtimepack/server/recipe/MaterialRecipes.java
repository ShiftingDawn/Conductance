package conductance.runtimepack.server.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterialTraits;
import conductance.api.material.Material;
import conductance.api.plugin.RecipeBuilderFactory;
import conductance.api.util.MiscUtils;
import conductance.core.register.MaterialOverrideRegister;
import static conductance.runtimepack.server.recipe.RecipeLoader.shapeless;

final class MaterialRecipes {

	public static void add(final RecipeOutput output, final RecipeBuilderFactory builderFactory) {
		CAPI.regs().materials().forEach(material -> {
			material.executeIf(NCMaterialTraits.ORE, $ -> MaterialRecipes.addOreRecipes(output, material));
			material.executeIf(NCMaterialFlags.GENERATE_PLATE, () -> MaterialRecipes.addPlateRecipes(output, material));
		});
	}

	private static void addOreRecipes(final RecipeOutput output, final Material material) {
		if (!MaterialOverrideRegister.has(NCMaterialTaggedSets.RAW_ORE_BLOCK, material)) {
			shapeless(output, "raw_%s_ore_block".formatted(material.getName()), CAPI.materials().getBlock(NCMaterialTaggedSets.RAW_ORE_BLOCK, material, 1),
					CAPI.materials().getItem(NCMaterialTaggedSets.RAW_ORE, material, 9));
		}
		if (!MaterialOverrideRegister.has(NCMaterialTaggedSets.RAW_ORE, material)) {
			shapeless(output, "raw_%s_ore".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.RAW_ORE, material, 9),
					CAPI.materials().getBlock(NCMaterialTaggedSets.RAW_ORE_BLOCK, material, 1));
		}
	}

	private static void addPlateRecipes(final RecipeOutput output, final Material material) {
		if (material.hasTrait(NCMaterialTraits.INGOT)) {
			shapeless(output, "%s_plate".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.PLATE, material, 1),
					'H', MiscUtils.getItemTag(NCMaterialTaggedSets.INGOT, material), 2);
			shapeless(output, "double_%s_plate".formatted(material.getName()), CAPI.materials().getItem(NCMaterialTaggedSets.PLATE_DOUBLE, material, 1),
					'H', MiscUtils.getItemTag(NCMaterialTaggedSets.PLATE, material), 2);
		}
	}

	private MaterialRecipes() {
	}
}
