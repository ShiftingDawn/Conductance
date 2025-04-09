package conductance.api.machine.recipe;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;

public interface RecipeTypeBuilder {

	RecipeTypeBuilder setIO(int maxItemsIn, int maxFluidsIn, int maxItemsOut, int maxFluidsOut);

	RecipeTypeBuilder setIO(boolean input, IRecipeElementType<?> type, int max);

	RecipeTypeBuilder setProgressBar(String name, ProgressTexture.FillDirection direction);

	default RecipeTypeBuilder setProgressBar(final String name) {
		return this.setProgressBar(name, ProgressTexture.FillDirection.LEFT_TO_RIGHT);
	}

	RecipeTypeBuilder setProgressBarDirection(ProgressTexture.FillDirection direction);

	RecipeTypeBuilder setRecipeViewProgressBar(String name);

	NCRecipeType build();
}
