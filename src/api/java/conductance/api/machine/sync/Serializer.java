package conductance.api.machine.sync;

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
	public abstract Tag serialize(Reference ref);

	public abstract void deserialize(Reference ref, Tag tag);

	public abstract void toNetwork(Reference ref, RegistryFriendlyByteBuf buf);

	public abstract void fromNetwork(Reference ref, RegistryFriendlyByteBuf buf);

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
