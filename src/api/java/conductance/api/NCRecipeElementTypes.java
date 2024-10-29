package conductance.api;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.machine.recipe.IRecipeElementType;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCRecipeElementTypes {

	public static IRecipeElementType<SizedIngredient> ITEM;
	public static IRecipeElementType<SizedFluidIngredient> FLUID;

	private NCRecipeElementTypes() {
	}
}
