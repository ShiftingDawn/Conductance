package conductance.core.data;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiFunction;
import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class CollectionFieldValueHandler implements ManagedFieldValueHandler<Collection> {

	static final BiFunction<ManagedFieldValueHandler<?>, Class<?>, CollectionFieldValueHandler> FACTORY = Util.memoize(CollectionFieldValueHandler::new);

	public CollectionFieldValueHandler(final ManagedFieldValueHandler<?> contentsHandler, final Class<?> contentsType) {
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return Collection.class.isAssignableFrom(type);
	}

	@Override
	public boolean equals(final Collection value1, final Collection value2) {
		return value1.size() != value2.size() || !new HashSet<>(value1).equals(new HashSet<>(value2));
	}

	@Override
	public @Nullable Collection readFromField(final Field field, final Object instance) {
		try {
			return (Collection) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeToField(final Field field, final Object instance, @Nullable final Collection value) {
		final Collection current = this.readFromField(field, instance);
		if (current == null) {
			return;
		}
		current.clear();
		if (value != null) {
			current.addAll(value);
		}
	}
}
