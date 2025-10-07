package conductance.api.machine.multi;

import net.minecraft.core.BlockPos;

public interface IMultiBlockPart {

	MultiBlockPartCapability getPartCapability();

	boolean isConnectedTo(BlockPos controllerPos);

	void setConnectedTo(BlockPos controllerPos, boolean connect);
}
