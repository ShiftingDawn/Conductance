package conductance.init;

import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.recipe.RecipeModifier;
import conductance.api.plugin.RecipeElementTypeRegister;

public final class ConductanceRecipeElementTypes {

	public static void init(final RecipeElementTypeRegister register) {
		NCRecipeElementTypes.ITEM = register.register("item", SizedIngredient.FLAT_CODEC, SizedIngredient.STREAM_CODEC, ConductanceRecipeElementTypes::copyItem);
		NCRecipeElementTypes.FLUID = register.register("fluid", SizedFluidIngredient.FLAT_CODEC, SizedFluidIngredient.STREAM_CODEC, ConductanceRecipeElementTypes::copyFluid);
	}

	private static SizedIngredient copyItem(final SizedIngredient obj, final RecipeModifier mod) {
		if (obj.ingredient().isEmpty()) {
			return new SizedIngredient(Ingredient.EMPTY, 1);
		}
		return new SizedIngredient(obj.ingredient(), mod.apply(obj.count()).intValue());
	}

	private static SizedFluidIngredient copyFluid(final SizedFluidIngredient obj, final RecipeModifier mod) {
		if (obj.ingredient().isEmpty()) {
			return new SizedFluidIngredient(FluidIngredient.empty(), 1);
		}
		return new SizedFluidIngredient(obj.ingredient(), mod.apply(obj.amount()).intValue());
	}

	private ConductanceRecipeElementTypes() {
	}
}
