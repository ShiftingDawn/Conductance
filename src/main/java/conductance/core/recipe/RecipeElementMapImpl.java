package conductance.core.recipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElement;
import conductance.api.machine.recipe.RecipeElementMap;
import conductance.api.util.DelegateMap;

public class RecipeElementMapImpl extends DelegateMap<IRecipeElementType<?>, List<RecipeElement>> implements RecipeElementMap {

	public RecipeElementMapImpl(final Map<IRecipeElementType<?>, List<RecipeElement>> delegate) {
		super(delegate);
	}

	public RecipeElementMapImpl() {
		this(new HashMap<>());
	}

	public final RecipeElementMap immutableCopy() {
		return new RecipeElementMapImpl(Collections.unmodifiableMap(this));
	}
}
