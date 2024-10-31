package conductance.api.machine.data.serializer;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;

public class NbtDataSerializer extends ObjectDataSerializer<Tag> {

	@Override
	public Tag serialize(final HolderLookup.Provider registries) {
		return this.getData();
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		this.setData(tag);
	}

	public static NbtDataSerializer of(final Tag tag) {
		return Util.make(new NbtDataSerializer(), serializer -> serializer.setData(tag));
	}
}
