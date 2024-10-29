package conductance.init;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.NCRecipeElementTypes;
import conductance.api.plugin.RecipeElementTypeRegister;

public final class ConductanceRecipeElementTypes {

	public static void init(final RecipeElementTypeRegister register) {
		NCRecipeElementTypes.ITEM = register.register("item", SizedIngredient.FLAT_CODEC, SizedIngredient.STREAM_CODEC);
		NCRecipeElementTypes.FLUID = register.register("fluid", SizedFluidIngredient.FLAT_CODEC, SizedFluidIngredient.STREAM_CODEC);
	}

	private ConductanceRecipeElementTypes() {
	}
}
