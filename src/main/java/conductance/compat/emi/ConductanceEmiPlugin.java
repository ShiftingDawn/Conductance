package conductance.compat.emi;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import conductance.api.machine.recipe.NCRecipeType;

@EmiEntrypoint
public final class ConductanceEmiPlugin implements EmiPlugin {

	@Override
	public void register(final EmiRegistry registry) {
		NCEmiRecipeCategory.register(registry);
		registry.addRecipeHandler(ModularUIContainer.MENUTYPE, new NCEmiRecipeHandler());
	}

	public static void displayEmiCategory(final NCRecipeType recipeType) {
		EmiApi.displayRecipeCategory(NCEmiRecipeCategory.CATEGORIES.apply(recipeType));
	}
}
