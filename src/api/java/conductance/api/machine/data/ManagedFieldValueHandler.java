package conductance.api.machine.data;

import java.lang.reflect.Field;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public interface ManagedFieldValueHandler<T> {

	boolean canHandle(Class<?> type);

	@Nullable
	T getValue(Field field, Object instance);

	void setValue(Field field, Object instance, @Nullable T value);

	boolean equals(T value1, T value2);

	Tag serialize(T value, HolderLookup.Provider registries);

	@Nullable
	T deserialize(Tag nbt, HolderLookup.Provider registries);

	@Nullable
	default T deserialize(final Class<T> fieldType, final Tag nbt, final HolderLookup.Provider registries) {
		return this.deserialize(nbt, registries);
	}
}
