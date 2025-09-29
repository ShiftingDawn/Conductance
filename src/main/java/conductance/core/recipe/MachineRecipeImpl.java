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
import conductance.api.recipe.RecipeElementType;
import conductance.api.recipe.RecipeObject;

final class MachineRecipeImpl implements MachineRecipe {

	private final MachineRecipeType recipeType;
	private final @Getter Map<RecipeElementType<?>, List<RecipeObject>> inputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeObject>> outputs;
	private final @Getter int recipeDuration;
	private final @Getter int program;

	MachineRecipeImpl(
		final MachineRecipeType recipeType,
		final Map<RecipeElementType<?>, List<RecipeObject>> inputs, final Map<RecipeElementType<?>, List<RecipeObject>> outputs,
		final int recipeDuration, final int program
	) {
		this.recipeType = recipeType;
		this.inputs = MachineRecipeImpl.toImmutableMap(inputs);
		this.outputs = MachineRecipeImpl.toImmutableMap(outputs);
		this.recipeDuration = recipeDuration;
		this.program = program;
	}

	@Override
	public RecipeSerializer<MachineRecipe> getSerializer() {
		return MachineRecipeSerializer.INSTANCE;
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

	private static Map<RecipeElementType<?>, List<RecipeObject>> toImmutableMap(final Map<RecipeElementType<?>, List<RecipeObject>> input) {
		final Map<RecipeElementType<?>, List<RecipeObject>> map = new IdentityHashMap<>();
		for (final Map.Entry<RecipeElementType<?>, List<RecipeObject>> entry : input.entrySet()) {
			map.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
		}
		return Collections.unmodifiableMap(map);
	}
}
