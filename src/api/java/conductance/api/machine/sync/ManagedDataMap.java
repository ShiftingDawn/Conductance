package conductance.api.machine.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface ManagedDataMap {

	CompoundTag serialize(Operation operation, HolderLookup.Provider registries);

	void deserialize(Operation operation, CompoundTag nbt, HolderLookup.Provider registries);

	void toNetwork(Operation operation, RegistryFriendlyByteBuf buf, HolderLookup.Provider registries);

	void fromNetwork(Operation operation, RegistryFriendlyByteBuf buf, HolderLookup.Provider registries);

	void tick();

	void markDirty();

	boolean isDirty();
}
