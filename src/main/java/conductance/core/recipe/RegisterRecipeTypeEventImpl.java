package conductance.core.recipe;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import conductance.api.recipe.NCRecipeType;
import conductance.api.recipe.RecipeTypeBuilder;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;

@AllArgsConstructor
final class RegisterRecipeTypeEventImpl implements RegisterRecipeTypeEvent {

	private final BiFunction<String, Consumer<RecipeTypeBuilder>, NCRecipeType> delegate;

	@Override
	public NCRecipeType register(final String registryName, final Consumer<RecipeTypeBuilder> builder) {
		return this.delegate.apply(registryName, builder);
	}
}
