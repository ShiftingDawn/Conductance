package conductance.api;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.MachineRecipeModifier;
import conductance.api.recipe.RecipeModifier;
import conductance.api.tier.Tier;

public final class NCRecipeModifiers {

	public static final MachineRecipeModifier LARGE_BOILER = recipe -> recipe.applyModifier(RecipeModifier.multiply(4), null, RecipeModifier.multiply(4), RecipeModifier.multiply(8));

	public static @Nullable MachineRecipeModifier steamTurbine(final Tier tier) {
		if (tier == NCTiers.LV) {
			return null;
		}
		return recipe -> {
			final long recipeVoltage = recipe.getPerTickOutputs().getOrDefault(NCRecipeElementTypes.ENERGY, List.of())
				.stream().mapToLong(elem -> (long) elem.data()).sum();
			return recipe.applyModifier(RecipeModifier.multiply((double) tier.getVoltage() / recipeVoltage), true, true, true, true);
		};
	}

	private NCRecipeModifiers() {
	}
}
