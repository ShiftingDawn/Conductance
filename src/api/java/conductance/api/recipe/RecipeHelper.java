package conductance.api.recipe;

import java.util.List;
import java.util.Map;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.util.IO;

public final class RecipeHelper {

	public boolean test(final MachineRecipe recipe, final RecipeCapabilityHolder holder) {
		final boolean ins = this.testInternal(recipe, holder, IO.IN, recipe.getInputs());
		final boolean outs = this.testInternal(recipe, holder, IO.OUT, recipe.getOutputs());
		return ins & outs;
	}

	private boolean testInternal(final MachineRecipe recipe, final RecipeCapabilityHolder holder, final IO io, final Map<RecipeElementType<?>, List<RecipeObject>> map) {
		for (final Map.Entry<RecipeElementType<?>, List<RecipeObject>> entry : map.entrySet()) {
			final List<MachineRecipeCapability<?>> handlers = holder.getRecipeCapabilities(entry.getKey(), io);
			if (handlers.isEmpty() && !entry.getValue().isEmpty()) {
				return false;
			}
			List recipeObjectContentList = entry.getValue().stream().map(RecipeObject::data).toList();
			for (final MachineRecipeCapability handler : handlers) {
				recipeObjectContentList = handler.handle(io, recipe, recipeObjectContentList, true);
				if (recipeObjectContentList == null || recipeObjectContentList.isEmpty()) {
					break;
				}
			}
			if (recipeObjectContentList != null && !recipeObjectContentList.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void handle(final MachineRecipe recipe, final IO io, final RecipeCapabilityHolder holder) {
		this.handleInternal(recipe, holder, io, switch (io) {
			case IN -> recipe.getInputs();
			case OUT -> recipe.getOutputs();
		});
	}

	private void handleInternal(final MachineRecipe recipe, final RecipeCapabilityHolder holder, final IO io, final Map<RecipeElementType<?>, List<RecipeObject>> map) {
		for (final Map.Entry<RecipeElementType<?>, List<RecipeObject>> entry : map.entrySet()) {
			final List<MachineRecipeCapability<?>> handlers = holder.getRecipeCapabilities(entry.getKey(), io);
			if (handlers.isEmpty() && !entry.getValue().isEmpty()) {
				continue;
			}
			List recipeObjectContentList = entry.getValue().stream().map(RecipeObject::data).toList();
			for (final MachineRecipeCapability handler : handlers) {
				recipeObjectContentList = handler.handle(io, recipe, recipeObjectContentList, false);
				if (recipeObjectContentList == null || recipeObjectContentList.isEmpty()) {
					break;
				}
			}
		}
	}
}
