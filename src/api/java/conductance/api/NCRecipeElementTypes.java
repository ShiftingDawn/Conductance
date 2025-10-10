package conductance.api;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.recipe.RecipeElementType;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCRecipeElementTypes {

	public static RecipeElementType<SizedIngredient> ITEM;
	public static RecipeElementType<SizedFluidIngredient> FLUID;
	public static RecipeElementType<Long> ENERGY;

	private NCRecipeElementTypes() {
	}
}
