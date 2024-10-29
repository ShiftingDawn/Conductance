package conductance.core.datapack.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterialTraits;
import conductance.api.material.Material;
import conductance.core.register.MaterialOverrideRegister;
import static conductance.core.datapack.recipe.RecipeLoader.shapeless;

final class MaterialRecipes {

	public static void add(final RecipeOutput output) {
		CAPI.regs().materials().forEach(material -> {
			material.executeIf(NCMaterialTraits.ORE, $ -> MaterialRecipes.addOreRecipes(output, material));
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

	private MaterialRecipes() {
	}
}
