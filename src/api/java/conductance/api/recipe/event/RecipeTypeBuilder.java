package conductance.api.recipe.event;

import conductance.api.NCRecipeElementTypes;
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
}
