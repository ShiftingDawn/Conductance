package conductance.compat;

import net.neoforged.fml.ModList;
import conductance.api.recipe.MachineRecipeType;
import conductance.compat.jei.ConductanceJeiPlugin;

public final class CompatHelper {

	public static boolean isJeiLoaded() {
		return ModList.get().isLoaded("jei");
	}

	public static void showRecipes(final MachineRecipeType recipeType) {
		if (CompatHelper.isJeiLoaded()) {
			ConductanceJeiPlugin.showRecipes(recipeType);
		}
	}

	private CompatHelper() {
	}
}
