package conductance.api.machine.data.serializer;

import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public final class UuidSerializer extends ObjectDataSerializer<UUID> {

	@Override
	public Tag serialize(final HolderLookup.Provider registries) {
		return StringTag.valueOf(this.getData().toString());
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		this.setData(UUID.fromString(tag.getAsString()));
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf) {
		buf.writeUUID(this.getData());
	}

	@Override
	public void fromNetwork(final RegistryFriendlyByteBuf buf) {
		this.setData(buf.readUUID());
	}
}
