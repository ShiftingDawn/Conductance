package conductance.core.data;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.data.ManagedFieldValueHandler;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class ArrayFieldValueHandler implements ManagedFieldValueHandler<Object> {

	static final BiFunction<ManagedFieldValueHandler<?>, Class<?>, ArrayFieldValueHandler> FACTORY = Util.memoize(ArrayFieldValueHandler::new);

	private final ManagedFieldValueHandler contentsHandler;
	private final Class<?> contentsType;

	public ArrayFieldValueHandler(final ManagedFieldValueHandler<?> contentsHandler, final Class<?> contentsType) {
		this.contentsHandler = contentsHandler;
		this.contentsType = contentsType;
	}

	@Override
	public boolean canHandle(final Class<?> type) {
		return type.isArray();
	}

	@Override
	public @Nullable Object getValue(final Field field, final Object instance) {
		try {
			return field.get(instance);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setValue(final Field field, final Object instance, @Nullable final Object value) {
		try {
			Object current = this.getValue(field, instance);
			if (current == null) {
				current = Array.newInstance(this.contentsType, Array.getLength(value));
				field.set(instance, current);
			}
			for (int i = 0; i < Array.getLength(current); ++i) {
				if (i >= Array.getLength(value)) {
					break;
				}
				Array.set(current, i, Array.get(value, i));
			}
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(final Object value1, final Object value2) {
		if (Array.getLength(value1) != Array.getLength(value2)) {
			return false;
		}
		return Arrays.equals((Object[]) value1, (Object[]) value2);
	}

	@Override
	public Tag serialize(final Object value, final HolderLookup.Provider registries) {
		final ListTag result = new ListTag();
		for (int i = 0; i < Array.getLength(value); ++i) {
			final CompoundTag tag = new CompoundTag();
			tag.putInt("i", i);
			tag.put("d", this.contentsHandler.serialize(Array.get(value, i), registries));
			result.add(tag);
		}
		return result;
	}

	@Override
	public @Nullable Object deserialize(final Tag nbt, final HolderLookup.Provider registries) {
		final ListTag list = (ListTag) nbt;
		final Object[] array = (Object[]) Array.newInstance(this.contentsType, list.size());
		for (int i = 0; i < array.length; ++i) {
			final CompoundTag tag = list.getCompound(i);
			final int index = tag.getInt("i");
			final Object value = this.contentsHandler.deserialize(this.contentsType, tag.get("d"), registries);
			if (index >= 0 && index < array.length) {
				array[index] = value;
			}
		}
		return array;
	}
}
