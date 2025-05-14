package conductance.api.machine.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

public abstract class Serializer<T> {

	@Getter
	@Setter
	private T data;

	@Nullable
	public abstract Tag serialize(Operation operation, Reference ref, HolderLookup.Provider registries);

	public abstract void deserialize(Operation operation, Reference ref, Tag tag, HolderLookup.Provider registries);

	public abstract void toNetwork(Operation operation, Reference ref, RegistryFriendlyByteBuf buf, HolderLookup.Provider registries);

	public abstract void fromNetwork(Operation operation, Reference ref, RegistryFriendlyByteBuf buf, HolderLookup.Provider registries);

	protected <A extends Tag> A testTag(final Tag tag, final Class<A> expectedType) {
		if (expectedType.isAssignableFrom(tag.getClass())) {
			return expectedType.cast(tag);
		}
		throw new IllegalArgumentException("Expected Tag of type %s, got: %s".formatted(expectedType.getName(), tag.getClass().getName()));
	}

	public final int getSid() {
		return CAPI.syncHelper().getSerializerId(this);
	}
}
