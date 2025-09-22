package conductance.core.recipe;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.event.RecipeTypeBuilder;
import conductance.api.recipe.event.RegisterRecipeTypeEvent;

@RequiredArgsConstructor
final class RegisterRecipeTypeEventImpl implements RegisterRecipeTypeEvent {

	private final BiFunction<String, Consumer<RecipeTypeBuilder>, MachineRecipeType> delegate;

	@Override
	public MachineRecipeType register(final String registryName, final Consumer<RecipeTypeBuilder> builder) {
		return this.delegate.apply(registryName, builder);
	}
}
