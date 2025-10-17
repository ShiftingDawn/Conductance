package conductance.api.recipe.event;

import net.minecraft.resources.ResourceLocation;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.CapIO;
import conductance.api.machine.gui.ProgressProvider;
import conductance.api.recipe.RecipeDataToken;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public interface RecipeTypeBuilder {

	RecipeTypeBuilder setIO(IO io, RecipeElementType<?> type, int limit);

	default RecipeTypeBuilder setIO(final int inItemsLimit, final int inFluidsLimit, final int outItemsLimit, final int outFluidsLimit) {
		return this.setIO(IO.IN, NCRecipeElementTypes.ITEM, inItemsLimit)
			.setIO(IO.IN, NCRecipeElementTypes.FLUID, inFluidsLimit)
			.setIO(IO.OUT, NCRecipeElementTypes.ITEM, outItemsLimit)
			.setIO(IO.OUT, NCRecipeElementTypes.FLUID, outFluidsLimit);
	}

	default RecipeTypeBuilder setEnergyIO(final CapIO io) {
		if (io.isInput()) {
			this.setIO(IO.IN, NCRecipeElementTypes.ENERGY, 1);
		}
		if (io.isOutput()) {
			this.setIO(IO.OUT, NCRecipeElementTypes.ENERGY, 1);
		}
		return this;
	}

	RecipeTypeBuilder data(RecipeDataToken<?> token);

	RecipeTypeBuilder recipeTestCallback(RecipeTestCallback.When when, RecipeTestCallback callback);

	RecipeTypeBuilder recipePerTickTestCallback(RecipeTestCallback.When when, RecipeTestCallback callback);

	RecipeTypeBuilder guiArrow(ResourceLocation arrowTexture, ProgressProvider.Direction direction);

	default RecipeTypeBuilder guiArrow(final ResourceLocation arrowTexture) {
		return this.guiArrow(arrowTexture, ProgressProvider.Direction.LEFT_TO_RIGHT);
	}

	RecipeTypeBuilder hidden();

	RecipeTypeBuilder recipeBuilderCallback(RecipeBuilderCallback callback);
}
