package nl.appelgebakje22.xdata.ref;

import java.lang.reflect.Array;
import nl.appelgebakje22.xdata.api.Holder;
import org.jetbrains.annotations.Nullable;

public abstract class ArrayHolder implements Holder {

	private ArrayHolder() {
	}

	public static ArrayHolder of(final Object array, final int index, final Class<?> c) {
		if (c == boolean.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getBoolean(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setBoolean(array, index, (boolean) object);
				}
			};
		} else if (c == byte.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getByte(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setByte(array, index, (byte) object);
				}
			};
		} else if (c == short.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getShort(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setShort(array, index, (short) object);
				}
			};
		} else if (c == int.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getInt(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setInt(array, index, (int) object);
				}
			};
		} else if (c == long.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getLong(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setLong(array, index, (long) object);
				}
			};
		} else if (c == float.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getFloat(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setFloat(array, index, (float) object);
				}
			};
		} else if (c == double.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getDouble(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setDouble(array, index, (double) object);
				}
			};
		} else if (c == char.class) {
			return new ArrayHolder() {

				@Override
				public Object get() {
					return Array.getChar(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.setChar(array, index, (char) object);
				}
			};
		} else {
			return new ArrayHolder() {

				@Override
				public @Nullable Object get() {
					return Array.get(array, index);
				}

				@Override
				public void set(@Nullable final Object object) {
					Array.set(array, index, object);
				}
			};
		}
	}
}
