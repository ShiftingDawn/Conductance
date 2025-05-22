package conductance.init.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

public class TagSerializer extends Serializer<Tag> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.getData();
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, @Nullable final Tag tag, final HolderLookup.Provider registries) {
		this.setData(tag);
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.write(buf, buf::writeNbt);
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.read(buf, buf::readNbt);
	}
}
