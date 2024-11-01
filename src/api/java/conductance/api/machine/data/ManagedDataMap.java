package conductance.api.machine.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface ManagedDataMap {

	void saveAllData(CompoundTag tag, HolderLookup.Provider registries);

	void readAllData(CompoundTag tag, HolderLookup.Provider registries);

	void writeToNetwork(RegistryFriendlyByteBuf buf, boolean forceSync);

	void readFromNetwork(RegistryFriendlyByteBuf buf);
}
