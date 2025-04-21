package conductance.api.machine;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

@SuppressWarnings("ConstantValue")
public interface IBlockEntity extends RunnableContainer, EnvironmentProvider {

	void onLoad();

	void onUnload();

	/**
	 * @return <code>true</code> if the BlockEntity should be considered invalid, <code>false</code> otherwise
	 * @see #isValid()
	 */
	default boolean isInvalid() {
		return ((BlockEntity) this).isRemoved();
	}

	/**
	 * @return <code>true</code> if the BlockEntity should be considered valid, <code>false</code> otherwise
	 * @see #isInvalid()
	 */
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

	default void onClientTick() {
	}

	void handleServerTick();

	default void onAnimateTick(final RandomSource random) {
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
