package conductance.api.machine.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface ManagedDataMap {

	CompoundTag serialize(Operation operation, HolderLookup.Provider registries);

	void deserialize(Operation operation, CompoundTag tag, HolderLookup.Provider registries);

	void tick();

	void markDirty();

	boolean isDirty();
}
