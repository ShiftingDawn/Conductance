package conductance.api.machine.api;

import net.minecraft.world.level.block.entity.BlockEntity;
import conductance.api.NCBlockStateProperties;

public interface IWorkable {

	default boolean getDefaultWorkingState() {
		return false;
	}

	default void setWorking(final boolean working) {
		if (this instanceof final IControllable controllable && !controllable.isProcessingAllowed()) {
			if (working || !this.isWorking()) {
				return;
			}
		}
		if (working != this.isWorking() && this instanceof final BlockEntity blockEntity && blockEntity.getLevel() != null) {
			blockEntity.getLevel().setBlockAndUpdate(blockEntity.getBlockPos(), blockEntity.getBlockState().setValue(NCBlockStateProperties.WORKING, working));
		}
	}

	default boolean isWorking() {
		if (this instanceof final IControllable controllable && !controllable.isProcessingAllowed()) {
			return false;
		}
		return this instanceof final BlockEntity blockEntity && blockEntity.getBlockState().getValue(NCBlockStateProperties.WORKING);
	}
}
