package conductance.core.sync;

import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

final class UUIDSerializer extends Serializer<UUID> {

	@Override
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return StringTag.valueOf(this.getData().toString());
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		final StringTag stringTag = this.testTag(tag, StringTag.class);
		this.setData(UUID.fromString(stringTag.getAsString()));
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		buf.writeUUID(this.getData());
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.setData(buf.readUUID());
	}
}
