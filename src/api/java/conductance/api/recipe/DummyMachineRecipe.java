package conductance.api.recipe;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

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
	public DummyMachineRecipe applyModifier(
		@Nullable final RecipeModifier inputMod, @Nullable final RecipeModifier outputMod, @Nullable final RecipeModifier perTickInputMod, @Nullable final RecipeModifier perTickOutputMod
	) {
		return new DummyMachineRecipe(
			this.getType(),
			MachineRecipe.applyModifierToContentMap(Objects.requireNonNullElseGet(inputMod, RecipeModifier::copy), this.getInputs()),
			MachineRecipe.applyModifierToContentMap(Objects.requireNonNullElseGet(outputMod, RecipeModifier::copy), this.getOutputs()),
			MachineRecipe.applyModifierToContentMap(Objects.requireNonNullElseGet(perTickInputMod, RecipeModifier::copy), this.getPerTickInputs()),
			MachineRecipe.applyModifierToContentMap(Objects.requireNonNullElseGet(perTickOutputMod, RecipeModifier::copy), this.getPerTickOutputs()),
			this.getRecipeDuration(),
			this.getProgram(),
			this.getRecipeDataMap().copy()
		);
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
}
