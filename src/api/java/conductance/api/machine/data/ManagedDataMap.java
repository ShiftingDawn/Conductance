package conductance.api.machine.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface ManagedDataMap {

	void saveAllData(CompoundTag tag, HolderLookup.Provider registries);

	void readAllData(CompoundTag tag, HolderLookup.Provider registries);
}
