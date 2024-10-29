package conductance.api.machine;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IRedstoneHandler {

	boolean canConnectRedstone(@Nullable Direction direction);
}
