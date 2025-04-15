package conductance.api.machine.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import conductance.api.util.overclock.OverclockResult;

public interface IRecipe extends Recipe<RecipeInput> {

	ResourceLocation getId();

	@Override
	NCRecipeType getType();

	Map<IRecipeElementType<?>, List<RecipeElement>> getInputs();

	Map<IRecipeElementType<?>, List<RecipeElement>> getInputsPerTick();

	Map<IRecipeElementType<?>, List<RecipeElement>> getOutputs();

	Map<IRecipeElementType<?>, List<RecipeElement>> getOutputsPerTick();

	int getProcessTime();

	long getEnergyPerTick();

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

	IRecipe copy(@Nullable RecipeModifier modifier, boolean modifyProcessTime);

	IRecipe copy(OverclockResult overclockResult, boolean modifyProcessTime);

	IRecipe copyMutable();
}
