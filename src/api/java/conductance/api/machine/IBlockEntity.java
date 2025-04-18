package conductance.api.machine;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("ConstantValue")
public interface IBlockEntity extends RunnableContainer, EnvironmentProvider {

	boolean isInvalid();

	default boolean isValid() {
		return !this.isInvalid();
	}

	@Override
	default void notifyBlockUpdate() {
		final Level level = this.getLevel();
		if (level != null) {
			level.updateNeighborsAt(this.getBlockPos(), level.getBlockState(this.getBlockPos()).getBlock());
		}
	}

	@Override
	default void scheduleRenderUpdate() {
		if (this.getLevel() != null) {
			final var state = this.getLevel().getBlockState(this.getBlockPos());
			if (this.isClientSide()) {
				this.getLevel().sendBlockUpdated(this.getBlockPos(), state, state, 1 << 3);
			} else {
				this.getLevel().blockEvent(this.getBlockPos(), state.getBlock(), 1, 0);
			}
		}
	}

	@Override
	default void scheduleNeighborShapeUpdate() {
		if (this.getLevel() != null && this.getBlockPos() != null) {
			this.getLevel().getBlockState(this.getBlockPos()).updateNeighbourShapes(this.getLevel(), this.getBlockPos(), Block.UPDATE_ALL);
		}
	}
}
