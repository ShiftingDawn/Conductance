package conductance.core.sync;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Holder;

final class ArrayHolder<T> implements Holder {

	private final Object array;
	private final int index;
	private final BiFunction<Object, Integer, T> getter;
	private final TriConsumer<Object, Integer, T> setter;

	private ArrayHolder(final Object array, final int index, final BiFunction<Object, Integer, T> getter, final TriConsumer<Object, Integer, T> setter) {
		this.getter = getter;
		this.setter = setter;
		this.array = array;
		this.index = index;
	}

	@Override
	@Nullable
	public Object get() {
		return this.getter.apply(this.array, this.index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(@Nullable final Object object) {
		this.setter.accept(this.array, this.index, (T) object);
	}

	public static Holder of(final Object array, final int index, final Class<?> c) {
		if (c == boolean.class) {
			return new ArrayHolder<>(array, index, Array::getBoolean, Array::setBoolean);
		} else if (c == byte.class) {
			return new ArrayHolder<>(array, index, Array::getByte, Array::setByte);
		} else if (c == short.class) {
			return new ArrayHolder<>(array, index, Array::getShort, Array::setShort);
		} else if (c == int.class) {
			return new ArrayHolder<>(array, index, Array::getInt, Array::setInt);
		} else if (c == long.class) {
			return new ArrayHolder<>(array, index, Array::getLong, Array::setLong);
		} else if (c == float.class) {
			return new ArrayHolder<>(array, index, Array::getFloat, Array::setFloat);
		} else if (c == double.class) {
			return new ArrayHolder<>(array, index, Array::getDouble, Array::setDouble);
		} else if (c == char.class) {
			return new ArrayHolder<>(array, index, Array::getChar, Array::setChar);
		} else {
			return new ArrayHolder<>(array, index, Array::get, Array::set);
		}
	}
}
