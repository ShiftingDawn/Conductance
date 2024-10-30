package conductance.core.data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class CollectionFieldValueHandler implements ManagedFieldValueHandler<Collection> {

	static final BiFunction<ManagedFieldValueHandler<?>, Class<?>, CollectionFieldValueHandler> FACTORY = Util.memoize(CollectionFieldValueHandler::new);

	private final ManagedFieldValueHandler contentsHandler;
	private final Class<?> contentsType;

	public CollectionFieldValueHandler(final ManagedFieldValueHandler<?> contentsHandler, final Class<?> contentsType) {
		this.contentsHandler = contentsHandler;
		this.contentsType = contentsType;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return Collection.class.isAssignableFrom(type);
	}

	@Override
	public @Nullable Collection getValue(final Field field, final Object instance) {
		try {
			return (Collection) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValue(final Field field, final Object instance, @Nullable final Collection value) {
		try {
			Collection current = this.getValue(field, instance);
			if (current == null) {
				current = (Collection) field.getType().getDeclaredConstructor().newInstance();
				field.set(instance, current);
			}
			current.clear();
			if (value != null) {
				current.addAll(value);
			}
		} catch (final IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(final Collection value1, final Collection value2) {
		return value1.size() != value2.size() || !new HashSet<>(value1).equals(new HashSet<>(value2));
	}

	@Override
	public Tag serialize(final Collection value, final HolderLookup.Provider registries) {
		return Util.make(new ListTag(), list -> value.forEach(item -> list.add(this.contentsHandler.serialize(item, registries))));
	}

	@Override
	public @Nullable Collection deserialize(final Tag nbt, final HolderLookup.Provider registries) {
		return ((ListTag) nbt).stream().map(tag -> this.contentsHandler.deserialize(this.contentsType, tag, registries)).filter(Objects::nonNull).toList();
	}
}
