package conductance.api.machine.trait;

import java.util.List;
import java.util.function.Predicate;
import lombok.Getter;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.util.IOMode;

public abstract class MetaRecipeCapability<T> extends MetaCapability implements Predicate<T> {

	@Getter
	private final IRecipeElementType<T> elementType;

	protected MetaRecipeCapability(final IRecipeElementType<T> elementType) {
		this.elementType = elementType;
	}

	public abstract List<T> handleRecipe(IOMode ioMode, IRecipe recipe, List<T> inputs, boolean simulate);
}
