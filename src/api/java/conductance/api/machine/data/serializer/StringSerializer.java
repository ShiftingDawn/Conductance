package conductance.api.machine.data.serializer;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public final class StringSerializer extends ObjectDataSerializer<String> {

	public StringSerializer(final String data) {
		this.setData(data);
	}

	@Override
	public @Nullable Tag serialize(final HolderLookup.Provider registries) {
		return StringTag.valueOf(this.getData());
	}

	@Override
	public void deserialize(final Tag tag, final HolderLookup.Provider registries) {
		this.setData(tag.getAsString());
	}
}
