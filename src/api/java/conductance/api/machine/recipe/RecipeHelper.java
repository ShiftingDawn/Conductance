package conductance.api.machine.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.IOMode;

public interface RecipeHelper {

	boolean test(IRecipe recipe, RecipeCapabilityHolder holder);

	boolean testPerTick(IRecipe recipe, RecipeCapabilityHolder holder);

	boolean handle(IRecipe recipe, IOMode ioMode, RecipeCapabilityHolder holder);

	boolean handlePerTick(IRecipe recipe, IOMode ioMode, RecipeCapabilityHolder holder);

	List<IRecipe> findRecipes(NCRecipeType recipeType, RecipeManager recipeManager, RecipeCapabilityHolder holder, @Nullable final Map<IRecipeElementType<?>, Integer> recipeOutputLimits);
}
