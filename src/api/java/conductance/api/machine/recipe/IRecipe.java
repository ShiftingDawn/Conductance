package conductance.api.machine.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public interface IRecipe extends Recipe<RecipeInput> {

	@Override
	NCRecipeType getType();

	Map<IRecipeElementType<?>, List<RecipeElement>> getInputs();

	Map<IRecipeElementType<?>, List<RecipeElement>> getInputsPerTick();

	Map<IRecipeElementType<?>, List<RecipeElement>> getOutputs();

	Map<IRecipeElementType<?>, List<RecipeElement>> getOutputsPerTick();

	@Override
	default boolean matches(final RecipeInput recipeInput, final Level level) {
		return false;
	}

	@Override
	default ItemStack assemble(final RecipeInput recipeInput, final HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(final int i, final int i1) {
		return false;
	}

	@Override
	default ItemStack getResultItem(final HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	default RecipeSerializer<?> getSerializer() {
		return this.getType().getSerializer();
	}
}
