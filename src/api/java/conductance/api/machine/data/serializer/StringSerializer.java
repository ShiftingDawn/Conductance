package conductance.api.machine.data.serializer;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class StringSerializer extends ObjectDataSerializer<String> {

	@Override
	public @Nullable Tag serialize(final HolderLookup.Provider registries) {
		return StringTag.valueOf(this.getData());
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		this.setData(tag.getAsString());
	}

	@Override
	public void toNetwork(final RegistryFriendlyByteBuf buf) {
		buf.writeUtf(this.getData());
	}

	@Override
	public void fromNetwork(final RegistryFriendlyByteBuf buf) {
		this.setData(buf.readUtf());
	}

	public static StringSerializer of(final String data) {
		return Util.make(new StringSerializer(), serializer -> serializer.setData(data));
	}
}
