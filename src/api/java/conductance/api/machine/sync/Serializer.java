package conductance.api.machine.sync;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
	@Nullable
	private T data;

	@Nullable
	public abstract Tag serialize(Operation operation, Reference ref, HolderLookup.Provider registries);

	public abstract void deserialize(Operation operation, Reference ref, @Nullable Tag tag, HolderLookup.Provider registries);

	public abstract void toNetwork(Operation operation, Reference ref, RegistryFriendlyByteBuf buf, HolderLookup.Provider registries);

	public abstract void fromNetwork(Operation operation, Reference ref, RegistryFriendlyByteBuf buf, HolderLookup.Provider registries);

	protected final <A extends Tag> A testTag(final Tag tag, final Class<A> expectedType) {
		if (expectedType.isAssignableFrom(tag.getClass())) {
			return expectedType.cast(tag);
		}
		throw new IllegalArgumentException("Expected Tag of type %s, got: %s".formatted(expectedType.getName(), tag.getClass().getName()));
	}

	@Nullable
	protected final <A> A serialize(final Function<T, A> func) {
		final T d = this.getData();
		return d != null ? func.apply(d) : null;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	protected final <A> A serializeRaw(final Reference ref, final Class<T> expectedType, final Function<T, A> func) {
		final Object obj = ref.getValueHolder().get();
		if (obj == null) {
			return null;
		}
		if (expectedType.isAssignableFrom(obj.getClass())) {
			return func.apply((T) obj);
		} else {
			throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), expectedType.getName()));
		}
	}

	protected final <A extends Tag> void deserialize(@Nullable final Tag tag, final Class<A> tagType, final Function<A, T> callback) {
		if (tag != null) {
			this.setData(callback.apply(this.testTag(tag, tagType)));
		}
	}

	@SuppressWarnings("unchecked")
	protected final <A extends Tag> void deserializeRaw(final Reference ref, final Class<T> expectedType, @Nullable final Tag tag, final Class<A> tagType, final BiConsumer<T, A> callback) {
		if (tag == null) {
			ref.getValueHolder().set(null);
			return;
		}
		final Object obj = ref.getValueHolder().get();
		if (obj == null) {
			throw new IllegalArgumentException("Field %s is null! If null is a valid value, use the %s annotation!".formatted(ref.getKey().getRawField(), SpecialHandled.class.getName()));
		}
		if (expectedType.isAssignableFrom(obj.getClass())) {
			callback.accept((T) obj, this.testTag(tag, tagType));
		} else {
			throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), expectedType.getName()));
		}
	}

	protected final void write(final RegistryFriendlyByteBuf buf, final Consumer<T> consumer) {
		final T d = this.getData();
		buf.writeBoolean(d != null);
		if (d != null) {
			consumer.accept(d);
		}
	}

	@SuppressWarnings("unchecked")
	protected final void writeRaw(final Reference ref, final Class<T> expectedType, final RegistryFriendlyByteBuf buf, final Consumer<T> consumer) {
		final Object obj = ref.getValueHolder().get();
		buf.writeBoolean(obj != null);
		if (obj != null) {
			if (expectedType.isAssignableFrom(obj.getClass())) {
				consumer.accept((T) obj);
			} else {
				throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), expectedType.getName()));
			}
		}
	}

	protected final void read(final RegistryFriendlyByteBuf buf, final Supplier<T> callback) {
		if (buf.readBoolean()) {
			this.setData(callback.get());
		}
	}

	@SuppressWarnings("unchecked")
	protected final void readRaw(final Reference ref, final Class<T> expectedType, final RegistryFriendlyByteBuf buf, final Consumer<T> callback) {
		if (!buf.readBoolean()) {
			ref.getValueHolder().set(null);
			return;
		}
		final Object obj = ref.getValueHolder().get();
		if (obj == null) {
			throw new IllegalArgumentException("Field %s is null! If null is a valid value, use the %s annotation!".formatted(ref.getKey().getRawField(), SpecialHandled.class.getName()));
		}
		if (expectedType.isAssignableFrom(obj.getClass())) {
			callback.accept((T) obj);
		} else {
			throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), expectedType.getName()));
		}
	}

	public final int getSid() {
		return CAPI.syncHelper().getSerializerId(this);
	}
}
