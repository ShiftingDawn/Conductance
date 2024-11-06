package nl.appelgebakje22.xdata.ref;

import java.lang.reflect.Field;
import lombok.Getter;
import lombok.SneakyThrows;
import nl.appelgebakje22.xdata.api.Holder;
import org.jetbrains.annotations.Nullable;

public abstract class ReflectionHolder implements Holder {

	private final Object instance;
	@Getter
	private final boolean primitive;

	private ReflectionHolder(final Object instance, final boolean primitive) {
		this.instance = instance;
		this.primitive = primitive;
	}

	public abstract Object get(Object instance) throws IllegalAccessException;

	public abstract void set(Object instance, Object data) throws IllegalAccessException;

	@SneakyThrows
	@Override
	public @Nullable Object get() {
		return this.get(this.instance);
	}

	@SneakyThrows
	@Override
	public void set(@Nullable final Object object) {
		this.set(this.instance, object);
	}

	public static ReflectionHolder of(final Field rawField, final Object instance) {
		final Class<?> c = rawField.getType();
		if (c == boolean.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getBoolean(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setBoolean(instance, (boolean) data);
				}
			};
		} else if (c == byte.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getByte(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setByte(instance, (byte) data);
				}
			};
		} else if (c == short.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getShort(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setShort(instance, (short) data);
				}
			};
		} else if (c == int.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getInt(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setInt(instance, (int) data);
				}
			};
		} else if (c == long.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getLong(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setLong(instance, (long) data);
				}
			};
		} else if (c == float.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getFloat(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setFloat(instance, (float) data);
				}
			};
		} else if (c == double.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getDouble(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setDouble(instance, (double) data);
				}
			};
		} else if (c == char.class) {
			return new ReflectionHolder(instance, true) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.getChar(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.setChar(instance, (char) data);
				}
			};
		} else {
			return new ReflectionHolder(instance, false) {

				@Override
				public Object get(final Object instance) throws IllegalAccessException {
					return rawField.get(instance);
				}

				@Override
				public void set(final Object instance, final Object data) throws IllegalAccessException {
					rawField.set(instance, data);
				}
			};
		}
	}
}
