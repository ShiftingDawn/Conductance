package conductance.core.sync.serializers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

public class ManagedSerializer extends Serializer<IManaged> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.getData().getDataMap().serialize(operation, registries);
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		final CompoundTag compoundTag = this.testTag(tag, CompoundTag.class);
		this.getData().getDataMap().deserialize(operation, compoundTag, registries);
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.getData().getDataMap().toNetwork(operation, buf, registries);
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.getData().getDataMap().fromNetwork(operation, buf, registries);
	}
}
