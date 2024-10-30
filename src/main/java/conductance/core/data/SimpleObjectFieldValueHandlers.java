package conductance.core.data;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

abstract class SimpleObjectFieldValueHandlers<T> implements ManagedFieldValueHandler<T> {

	private final Class<T> clazz;

	SimpleObjectFieldValueHandlers(final Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type == this.clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public @Nullable T getValue(final Field field, final Object instance) {
		try {
			return (T) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValue(final Field field, final Object instance, @Nullable final T value) {
		try {
			field.set(instance, value);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(final T value1, final T value2) {
		return Objects.equals(value1, value2);
	}

	public static class HandlerString extends SimpleObjectFieldValueHandlers<String> {

		HandlerString() {
			super(String.class);
		}

		@Override
		public Tag serialize(final String value) {
			return StringTag.valueOf(value);
		}

		@Override
		public @Nullable String deserialize(final Tag nbt) {
			return nbt.getAsString();
		}
	}

	public static class HandlerResourceLocation extends SimpleObjectFieldValueHandlers<ResourceLocation> {

		HandlerResourceLocation() {
			super(ResourceLocation.class);
		}

		@Override
		public Tag serialize(final ResourceLocation value) {
			return StringTag.valueOf(value.toString());
		}

		@Override
		public @Nullable ResourceLocation deserialize(final Tag nbt) {
			return ResourceLocation.parse(nbt.getAsString());
		}
	}

	public static class HandlerUUID extends SimpleObjectFieldValueHandlers<UUID> {

		HandlerUUID() {
			super(UUID.class);
		}

		@Override
		public Tag serialize(final UUID value) {
			return StringTag.valueOf(value.toString());
		}

		@Override
		public @Nullable UUID deserialize(final Tag nbt) {
			return UUID.fromString(nbt.getAsString());
		}
	}
}
