package conductance.api.machine.recipe;

public interface RecipeTypeBuilder {
	RecipeTypeBuilder setIO(int maxItemsIn, int maxFluidsIn, int maxItemsOut, int maxFluidsOut);

	RecipeTypeBuilder setIO(boolean input, IRecipeElementType<?> type, int max);

	NCRecipeType build();
}
