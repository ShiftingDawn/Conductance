package conductance.core.data;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.BiFunction;
import net.minecraft.Util;
import com.google.gson.internal.Primitives;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

public final class ArrayFieldValueHandler implements ManagedFieldValueHandler<Object> {

	static final BiFunction<ManagedFieldValueHandler<?>, Class<?>, ArrayFieldValueHandler> FACTORY = Util.memoize(ArrayFieldValueHandler::new);

	private final Class<?> contentType;

	public ArrayFieldValueHandler(final ManagedFieldValueHandler<?> contentHandler, final Class<?> contentType) {
		this.contentType = contentType.isPrimitive() ? Primitives.wrap(contentType) : contentType;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isArray();
	}

	@Override
	public boolean equals(final Object value1, final Object value2) {
		if (Array.getLength(value1) != Array.getLength(value2)) {
			return false;
		}
		return Arrays.equals((Object[]) value1, (Object[]) value2);
	}

	@Override
	@Nullable
	public Object readFromField(final Field field, final Object instance) {
		try {
			return field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void writeToField(final Field field, final Object instance, @Nullable final Object value) {
		final Object current = this.readFromField(field, instance);
		if (current == null) {
			return;
		}
		for (int i = 0; i < Array.getLength(current); ++i) {
			final boolean valid = value != null && i < Array.getLength(value);
			if (this.contentType == Boolean.class) {
				Array.setBoolean(current, i, valid && Array.getBoolean(value, i));
			} else if (this.contentType == Byte.class) {
				Array.setByte(current, i, valid ? Array.getByte(value, i) : 0);
			} else if (this.contentType == Short.class) {
				Array.setShort(current, i, valid ? Array.getShort(value, i) : 0);
			} else if (this.contentType == Integer.class) {
				Array.setInt(current, i, valid ? Array.getInt(value, i) : 0);
			} else if (this.contentType == Long.class) {
				Array.setLong(current, i, valid ? Array.getLong(value, i) : 0);
			} else if (this.contentType == Float.class) {
				Array.setFloat(current, i, valid ? Array.getFloat(value, i) : 0);
			} else if (this.contentType == Double.class) {
				Array.setDouble(current, i, valid ? Array.getDouble(value, i) : 0);
			} else if (this.contentType == Character.class) {
				Array.setChar(current, i, valid ? Array.getChar(value, i) : 0);
			} else {
				Array.set(current, i, valid ? Array.get(value, i) : null);
			}
		}
	}
}
