package conductance.core.sync;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.Operation;
import conductance.api.machine.sync.Reference;
import conductance.api.machine.sync.Serializer;

class ManagedSerializer extends Serializer<IManaged> {

	@Override
	@Nullable
	public Tag serialize(final Operation operation, final Reference ref, final HolderLookup.Provider registries) {
		if (ref.getValueHolder().get() instanceof final IManaged managed) {
			return managed.getDataMap().serialize(operation, registries);
		} else {
			throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), IManaged.class.getName()));
		}
	}

	@Override
	public void deserialize(final Operation operation, final Reference ref, final Tag tag, final HolderLookup.Provider registries) {
		final CompoundTag compoundTag = this.testTag(tag, CompoundTag.class);
		if (ref.getValueHolder().get() instanceof final IManaged managed) {
			managed.getDataMap().deserialize(operation, compoundTag, registries);
		} else {
			throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), IManaged.class.getName()));
		}
	}

	@Override
	public void toNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		if (ref.getValueHolder().get() instanceof final IManaged managed) {
			managed.getDataMap().toNetwork(operation, buf, registries);
		} else {
			throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), IManaged.class.getName()));
		}
	}

	@Override
	public void fromNetwork(final Operation operation, final Reference ref, final RegistryFriendlyByteBuf buf, final HolderLookup.Provider registries) {
		if (ref.getValueHolder().get() instanceof final IManaged managed) {
			managed.getDataMap().fromNetwork(operation, buf, registries);
		} else {
			throw new IllegalArgumentException("Field %s is not an instance of %s!".formatted(ref.getKey().getRawField(), IManaged.class.getName()));
		}
	}
}
