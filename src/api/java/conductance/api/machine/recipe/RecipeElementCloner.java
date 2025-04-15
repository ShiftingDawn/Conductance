package conductance.api.machine.recipe;

@FunctionalInterface
public interface RecipeElementCloner<T> {

	T copy(T obj, RecipeModifier mod);

	default T copy(final T obj) {
		return this.copy(obj, RecipeModifier.copy());
	}
}
