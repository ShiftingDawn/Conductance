package conductance.api.recipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import conductance.api.CAPI;

public final class DummyMachineRecipe extends MachineRecipe {

	public DummyMachineRecipe(
		final MachineRecipeType recipeType,
		final Map<RecipeElementType<?>, List<RecipeElement>> inputs, final Map<RecipeElementType<?>, List<RecipeElement>> outputs,
		final Map<RecipeElementType<?>, List<RecipeElement>> perTickInputs, final Map<RecipeElementType<?>, List<RecipeElement>> perTickOutputs,
		final int recipeDuration, final int program, final RecipeDataMap recipeDataMap
	) {
		super(recipeType, inputs, outputs, perTickInputs, perTickOutputs, recipeDuration, program, recipeDataMap);
	}

	@Override
	public RecipeSerializer<MachineRecipe> getSerializer() {
		throw new AssertionError("Dummy recipes should only be used directly!");
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC;
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	private static Map<RecipeElementType<?>, List<RecipeElement>> makeImmutable(final Map<RecipeElementType<?>, List<RecipeElement>> input) {
		return Collections.unmodifiableMap(CAPI.make(new HashMap<>(), map -> {
			input.forEach((key, value) -> map.put(key, Collections.unmodifiableList(value)));
		}));
	}
}
