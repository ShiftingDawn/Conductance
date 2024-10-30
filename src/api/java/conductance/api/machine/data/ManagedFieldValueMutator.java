package conductance.api.machine.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;

public interface ManagedFieldValueMutator<T> {

	boolean canHandle(Class<?> type);

	boolean equals(T value1, T value2);

	Tag serialize(T value, HolderLookup.Provider registries);

	void deserialize(T value, Tag nbt, HolderLookup.Provider registries);
}
