package conductance.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;
import conductance.api.util.tier.Tier;

public class TierSerializer extends Serializer<Tier> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		return this.serialize(data -> StringTag.valueOf(data.getRegistryKey()));
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		this.deserialize(tag, StringTag.class, t -> CAPI.regs().tiers().get(t.getAsString()));
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.write(buf, data -> buf.writeUtf(data.getRegistryKey()));
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		this.read(buf, () -> CAPI.regs().tiers().get(buf.readUtf()));
	}
}
