package conductance.api.machine.api;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IEventListener {

	default void onBlockStateChanged(final BlockState oldState, final BlockState newState) {
	}

	default void onNeighborChanged() {
	}

	default void onRotated(final Direction newFacing, final Direction oldFacing) {
	}

	default void onPlaced() {
	}

	default void onLoad() {
	}

	default void onUnload() {
	}

	default void onClientTick() {
	}
}
