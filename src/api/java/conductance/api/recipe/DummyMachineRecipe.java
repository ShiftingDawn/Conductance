package conductance.api.recipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import lombok.Getter;
import conductance.api.CAPI;

public final class DummyMachineRecipe implements MachineRecipe {

	private final @Getter MachineRecipeType type;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> inputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> outputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> perTickInputs;
	private final @Getter Map<RecipeElementType<?>, List<RecipeElement>> perTickOutputs;
	private final @Getter int recipeDuration;
	private final @Getter int program;

	public DummyMachineRecipe(
		final MachineRecipeType type,
		final Map<RecipeElementType<?>, List<RecipeElement>> inputs, final Map<RecipeElementType<?>, List<RecipeElement>> outputs,
		final Map<RecipeElementType<?>, List<RecipeElement>> perTickInputs, final Map<RecipeElementType<?>, List<RecipeElement>> perTickOutputs,
		final int recipeDuration, final int program
	) {
		this.type = type;
		this.inputs = DummyMachineRecipe.makeImmutable(inputs);
		this.outputs = DummyMachineRecipe.makeImmutable(outputs);
		this.perTickInputs = DummyMachineRecipe.makeImmutable(perTickInputs);
		this.perTickOutputs = DummyMachineRecipe.makeImmutable(perTickOutputs);
		this.recipeDuration = recipeDuration;
		this.program = program;
	}

	@Override
	public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
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
