package conductance.api.machine.data.serializer;

import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ObjectDataSerializer<T> implements DataSerializer<T> {

	private T data;

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf) {
		final Tag tag = this.serialize(buf.registryAccess());
		if (tag == null) {
			buf.writeBoolean(false);
		} else {
			buf.writeBoolean(true);
			buf.writeNbt(tag);
		}
	}

	@Override
	public void fromNetwork(final RegistryFriendlyByteBuf buf) {
		if (buf.readBoolean()) {
			final Tag tag = FriendlyByteBuf.readNbt(buf, NbtAccounter.create(2097152L));
			if (tag != null) {
				this.deserialize(tag, buf.registryAccess());
			}
		}
	}
}
