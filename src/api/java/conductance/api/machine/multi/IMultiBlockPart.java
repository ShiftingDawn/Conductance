package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;

public interface IMultiBlockPart {

	default boolean sharable() {
		return true;
	}

	boolean isConnectedTo(BlockPos controllerPos);

	void setConnectedTo(BlockPos controllerPos, boolean connect);
}
