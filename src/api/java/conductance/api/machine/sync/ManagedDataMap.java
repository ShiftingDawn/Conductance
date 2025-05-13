package conductance.api.machine.sync;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface ManagedDataMap {

	CompoundTag serialize(Operation operation);

	void deserialize(Operation operation, CompoundTag nbt);

	void toNetwork(Operation operation, RegistryFriendlyByteBuf buf);

	void fromNetwork(Operation operation, RegistryFriendlyByteBuf buf);
}
