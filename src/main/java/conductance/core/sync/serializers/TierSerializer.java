package conductance.core.sync.serializers;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;
import conductance.api.util.tier.Tier;

public class TierSerializer extends Serializer<Tier> {

	@Override
	@Nullable
	public Tag serialize(final Reference ref) {
		return StringTag.valueOf(this.getData().getRegistryKey());
	}

	@Override
	public void deserialize(final Reference ref, final Tag tag) {
		final StringTag stringTag = this.testTag(tag, StringTag.class);
		this.setData(CAPI.regs().tiers().get(stringTag.getAsString()));
	}

	@Override
	public void toNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		buf.writeUtf(this.getData().getRegistryKey());
	}

	@Override
	public void fromNetwork(final Reference ref, final RegistryFriendlyByteBuf buf) {
		this.setData(CAPI.regs().tiers().get(buf.readUtf()));
	}
}
