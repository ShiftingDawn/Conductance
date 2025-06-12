package conductance.compat.emi;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import org.jetbrains.annotations.Nullable;
import conductance.api.recipe.IRecipe;
import conductance.compat.ConductanceRecipeWidget;

final class NCEmiRecipe extends ModularEmiRecipe<WidgetGroup> {

	private final NCEmiRecipeCategory recipeCategory;
	private final RecipeHolder<IRecipe> recipe;

	NCEmiRecipe(final NCEmiRecipeCategory recipeCategory, final RecipeHolder<IRecipe> recipe) {
		super(() -> new ConductanceRecipeWidget(recipe));

		this.recipeCategory = recipeCategory;
		this.recipe = recipe;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return this.recipeCategory;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return this.recipe.id();
	}
}
