package conductance.init.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;
import conductance.api.sync.Operation;
import conductance.api.sync.Reference;
import conductance.api.sync.Serializer;

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
}
