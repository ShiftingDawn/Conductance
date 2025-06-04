package conductance.runtimepack.client;

import java.util.function.BiConsumer;
import lombok.AllArgsConstructor;
import conductance.api.resource.event.AddTranslationEvent;

@AllArgsConstructor
final class AddTranslationEventImpl implements AddTranslationEvent {

	private final BiConsumer<String, String> delegate;

	@Override
	public void add(final String key, final String value) {
		this.delegate.accept(key, value);
	}
}
