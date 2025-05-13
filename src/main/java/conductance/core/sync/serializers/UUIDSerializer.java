package conductance.core.sync.serializers;

import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import conductance.api.machine.sync.Serializer;
import conductance.api.machine.sync.Reference;
import org.jetbrains.annotations.Nullable;

public class UUIDSerializer extends Serializer<UUID> {

	@Override
	public @Nullable Tag serialize(final Reference ref) {
		return StringTag.valueOf(this.getData().toString());
	}

	@Override
	public void deserialize(final Reference ref, final Tag tag) {
		final StringTag stringTag = this.testTag(tag, StringTag.class);
		this.setData(UUID.fromString(stringTag.getAsString()));
	}

	@Override
	public void toNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		buf.writeUUID(this.getData());
	}

	@Override
	public void fromNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		this.setData(buf.readUUID());
	}

	public static UUIDSerializer of(final UUID data) {
		return Util.make(new UUIDSerializer(), serializer -> serializer.setData(data));
	}
}
