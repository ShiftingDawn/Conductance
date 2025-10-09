package conductance.core.recipe;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import lombok.Getter;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElement;
import conductance.api.recipe.RecipeElementType;

final class MachineRecipeImpl implements MachineRecipe {

	private final MachineRecipeType recipeType;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> inputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> outputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> perTickInputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> perTickOutputs;
	private final @Getter int recipeDuration;
	private final @Getter int program;

	MachineRecipeImpl(
		final MachineRecipeType recipeType,
		final Map<RecipeElementType<?>, List<RecipeElement>> inputs, final Map<RecipeElementType<?>, List<RecipeElement>> outputs,
		final Map<RecipeElementType<?>, List<RecipeElement>> perTickInputs, final Map<RecipeElementType<?>, List<RecipeElement>> perTickOutputs,
		final int recipeDuration, final int program
	) {
		this.recipeType = recipeType;
		this.inputs = MachineRecipeImpl.toImmutableMap(inputs);
		this.outputs = MachineRecipeImpl.toImmutableMap(outputs);
		this.perTickInputs = MachineRecipeImpl.toImmutableMap(perTickInputs);
		this.perTickOutputs = MachineRecipeImpl.toImmutableMap(perTickOutputs);
		this.recipeDuration = recipeDuration;
		this.program = program;
	}

	@Override
	public RecipeSerializer<MachineRecipe> getSerializer() {
		return this.recipeType.getRecipeSerializer();
	}

	@Override
	public MachineRecipeType getType() {
		return this.recipeType;
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		//TODO check if a custom one is needed here to "hide" all recipes
		return RecipeBookCategories.CRAFTING_MISC;
	}

	private static Map<RecipeElementType<?>, List<RecipeElement>> toImmutableMap(final Map<RecipeElementType<?>, List<RecipeElement>> input) {
		final Map<RecipeElementType<?>, List<RecipeElement>> map = new IdentityHashMap<>();
		for (final Map.Entry<RecipeElementType<?>, List<RecipeElement>> entry : input.entrySet()) {
			map.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
		}
		return Collections.unmodifiableMap(map);
	}
}
