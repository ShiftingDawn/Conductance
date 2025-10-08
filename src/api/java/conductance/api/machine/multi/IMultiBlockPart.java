package conductance.api.machine.multi;

import java.util.SortedSet;
import net.minecraft.core.BlockPos;

public interface IMultiBlockPart {

	MultiBlockPartCapability getPartCapability();

	SortedSet<BlockPos> getControllers();

	default boolean isConnectedTo(final BlockPos controllerPos) {
		return this.getControllers().contains(controllerPos);
	}

	void setConnectedTo(BlockPos controllerPos, boolean connect);
}
