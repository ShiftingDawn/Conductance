package conductance.api.machine.trait;

import java.util.List;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.NCRecipeType;

public final class RecipeProcessor {

	private final RecipeProcessingProvider provider;

	public RecipeProcessor(final RecipeProcessingProvider provider) {
		this.provider = provider;
	}

	@Nullable
	private List<RecipeHolder<IRecipe>> listRecipes(final RecipeManager recipeManager) {
		final int activeType = this.provider.getActiveRecipeType();
		if (activeType < 0) {
			return null;
		}
		final NCRecipeType recipeType = this.provider.getRecipeTypes().length > activeType ? this.provider.getRecipeTypes()[activeType] : null;
		if (recipeType == null) {
			return null;
		}
		return recipeManager.getAllRecipesFor(recipeType);
	}

	public boolean canProcess(final IRecipe recipe) {
		return false;
	}
}
