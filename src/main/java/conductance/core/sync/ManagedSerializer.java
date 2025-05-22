package conductance.core.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

class ManagedSerializer extends Serializer<IManaged> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.serializeRaw(ref, IManaged.class, managed -> managed.getDataMap().serialize(operation, registries));
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, @Nullable final Tag tag, final HolderLookup.Provider registries) {
		this.deserializeRaw(ref, IManaged.class, tag, CompoundTag.class, (managed, t) -> managed.getDataMap().deserialize(operation, t, registries));
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.writeRaw(ref, IManaged.class, buf, managed -> managed.getDataMap().toNetwork(operation, buf, registries));
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.readRaw(ref, IManaged.class, buf, managed -> managed.getDataMap().fromNetwork(operation, buf, registries));
	}
}
