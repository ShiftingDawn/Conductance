package conductance.api.machine.xdata;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.api.ReferenceHandler;
import nl.appelgebakje22.xdata.api.Serializer;
import nl.appelgebakje22.xdata.ref.Reference;

public final class BlockStateHandler implements ReferenceHandler {

	@Override
	public boolean canHandle(final Class<?> clazz) {
		return BlockState.class.isAssignableFrom(clazz);
	}

	@Override
	public Serializer<?> readFromReference(final Operation operation, final Reference ref) {
		final BlockState blockState = (BlockState) ref.getValueHolder().get();
		return XData.make(new NbtSerializer(), serializer -> serializer.setData(NbtUtils.writeBlockState(blockState)));
	}

	@Override
	public void writeToReference(final Operation operation, final Reference ref, final Serializer<?> rawSerializer) {
		final NbtSerializer serializer = this.testSerializer(rawSerializer, NbtSerializer.class);
		ref.getValueHolder().set(NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), (CompoundTag) serializer.getData()));
	}
}
