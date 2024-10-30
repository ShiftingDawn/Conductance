package conductance.core.data;

import java.lang.reflect.Field;
import java.util.Objects;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.data.ManagedFieldValueHandler;

abstract class BuiltInFieldValueHandlers<T> implements ManagedFieldValueHandler<T>  {

	private final Class<T> clazz;

	BuiltInFieldValueHandlers(final Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Class<T> getFieldType() {
		return this.clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getValue(final Field field, final Object instance) {
		try {
			return (T) field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(final T value1, final T value2) {
		return Objects.equals(value1, value2);
	}

	public static final class HandlerString extends BuiltInFieldValueHandlers<String> {

		HandlerString() {
			super(String.class);
		}

		@Override
		public Tag serialize(final String value) {
			return StringTag.valueOf(value);
		}

		@Override
		public String deserialize(final Tag nbt) {
			return nbt.getAsString();
		}
	}

	public static final class HandlerResourceLocation extends BuiltInFieldValueHandlers<ResourceLocation> {

		HandlerResourceLocation() {
			super(ResourceLocation.class);
		}

		@Override
		public Tag serialize(final ResourceLocation value) {
			return StringTag.valueOf(value.toString());
		}

		@Override
		public ResourceLocation deserialize(final Tag nbt) {
			return ResourceLocation.parse(nbt.getAsString());
		}
	}
}
