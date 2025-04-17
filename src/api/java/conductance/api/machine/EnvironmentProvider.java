package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.CAPI;

public interface EnvironmentProvider {

	IBlockEntity getBlockEntity();

	void onNeighborChanged(BlockPos neighborPos, BlockState neighborState, Direction neighborSide);

	default long getTimerOffset() {
		return this.getBlockEntity().getTimerOffset();
	}

	default boolean hasTicksPassed(final int offset) {
		return this.getTimerOffset() % offset == 0;
	}

	default void setChanged() {
		this.getBlockEntity().setChanged();
	}

	default void notifyBlockUpdate() {
		this.getBlockEntity().notifyBlockUpdate();
	}

	default void scheduleRenderUpdate() {
		this.getBlockEntity().notifyBlockUpdate();
	}

	default void scheduleNeighborShapeUpdate() {
		this.getBlockEntity().scheduleNeighborShapeUpdate();
	}

	default boolean isClientSide() {
		return this.getBlockEntity().getLevel() == null ? CAPI.isClient() : this.getLevel().isClientSide();
	}

	default BlockPos getBlockPos() {
		return this.getBlockEntity().getBlockPos();
	}

	default BlockState getBlockState() {
		return this.getBlockEntity().getBlockState();
	}

	default Level getLevel() {
		return this.getBlockEntity().getLevel();
	}
}
