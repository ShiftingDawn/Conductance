package conductance.api.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import com.mojang.serialization.Codec;

public interface MachineRecipe extends Recipe<RecipeInput> {

	Codec<Map<RecipeElementType<?>, List<RecipeObject>>> CONTENT_MAP_CODEC = Codec.dispatchedMap(RecipeElementType.CODEC, elementType -> elementType.getRecipeObjectCodec().listOf());

	@Override
	MachineRecipeType getType();

	@Override
	RecipeSerializer<MachineRecipe> getSerializer();

	Map<RecipeElementType<?>, List<RecipeObject>> getInputs();

	Map<RecipeElementType<?>, List<RecipeObject>> getOutputs();

	int getRecipeDuration();

	/**
	 * @return The required program for this recipe, or <code>-1</code> if no program has been set
	 */
	int getProgram();

	@Override
	default boolean isSpecial() {
		return true;
	}

	@Override
	@Deprecated
	default boolean matches(final RecipeInput recipeInput, final Level level) {
		return false;
	}

	@Override
	@Deprecated
	default ItemStack assemble(final RecipeInput recipeInput, final HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}
}
