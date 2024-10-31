package conductance.core.datahandlers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.data.handler.InstancedField;
import conductance.api.machine.data.handler.ManagedObjectValueHandler;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.api.machine.data.serializer.NbtDataSerializer;

public final class BlockStateValueHandler extends ManagedObjectValueHandler {

	public BlockStateValueHandler() {
		super(BlockState.class, false);
	}

	@Override
	public DataSerializer<?> readFromField(final InstancedField field, final HolderLookup.Provider registries) {
		return NbtDataSerializer.of(NbtUtils.writeBlockState((BlockState) field.get()));
	}

	@Override
	public void writeToField(final InstancedField field, final DataSerializer<?> value, final HolderLookup.Provider registries) {
		final NbtDataSerializer nbtDataSerializer = this.testSerializer(value, NbtDataSerializer.class);
		if (!(nbtDataSerializer.getData() instanceof final CompoundTag compoundTag)) {
			throw new IllegalStateException("Expected CompoundTag, got: " + nbtDataSerializer.getData());
		}
		field.set(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), compoundTag));
	}
}
