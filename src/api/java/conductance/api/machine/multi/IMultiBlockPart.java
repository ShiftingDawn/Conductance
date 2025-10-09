package conductance.api.machine.multi;

import java.util.SortedSet;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.util.IO;

public interface IMultiBlockPart {

	MultiBlockPartCapability getPartCapability();

	SortedSet<BlockPos> getControllers();

	default boolean isConnectedTo(final BlockPos controllerPos) {
		return this.getControllers().contains(controllerPos);
	}

	void setConnectedTo(BlockPos controllerPos, boolean connect);

	void attachCapabilities(BiConsumer<IO, MachineRecipeCapability<?>> consumer);
}
