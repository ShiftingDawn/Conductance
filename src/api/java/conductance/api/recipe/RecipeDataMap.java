package conductance.api.recipe;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.CAPI;

public final class RecipeDataMap {

	private final @Getter Map<String, RecipeDataToken.Pair<?>> data;

	public RecipeDataMap(final Map<String, RecipeDataToken.Pair<?>> data) {
		this.data = Collections.unmodifiableMap(data);
	}

	public RecipeDataMap(final List<RecipeDataToken.Pair<?>> data) {
		this(CAPI.make(new HashMap<>(), map -> data.forEach(pair -> map.put(pair.token().name(), pair))));
	}

	public RecipeDataMap(final RecipeDataMap toCopy) {
		this(List.copyOf(toCopy.data.values()));
	}

	@SuppressWarnings("unchecked")
	public <T> @UnknownNullability T get(final RecipeDataToken<T> token) {
		final RecipeDataToken.Pair<?> pair = this.data.get(token.name());
		return pair != null ? (T) pair.value() : null;
	}

	public RecipeDataMap copy() {
		return new RecipeDataMap(this);
	}
}
