package conductance.api.machine;

import net.minecraft.core.Direction;

public interface IRedstoneProvider extends IRedstoneHandler {

	int getRedstone(Direction side);

	int getRedstoneDirect(Direction side);

	int getRedstoneAnalog();
}
