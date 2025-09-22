package conductance.core.recipe;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

final class MachineRecipeTypeImpl implements MachineRecipeType {

	private final Object2IntMap<RecipeElementType<?>> inputLimits;
	private final Object2IntMap<RecipeElementType<?>> outputLimits;

	MachineRecipeTypeImpl(final Object2IntMap<RecipeElementType<?>> inputLimits, final Object2IntMap<RecipeElementType<?>> outputLimits) {
		this.inputLimits = Object2IntMaps.unmodifiable(inputLimits);
		this.outputLimits = Object2IntMaps.unmodifiable(outputLimits);
	}

	@Override
	public int getLimit(final IO io, final RecipeElementType<?> elementType) {
		return switch (io) {
			case IN -> this.inputLimits.getInt(elementType);
			case OUT -> this.outputLimits.getInt(elementType);
		};
	}
}
