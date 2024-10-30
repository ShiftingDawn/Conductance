package conductance.api.machine.data;

import net.minecraft.nbt.CompoundTag;

public interface ManagedDataMap {

	void saveAllData(CompoundTag tag);

	void readAllData(CompoundTag tag);
}
