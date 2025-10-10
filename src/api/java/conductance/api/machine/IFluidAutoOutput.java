package conductance.api.machine;

import net.minecraft.core.Direction;

public interface IFluidAutoOutput {

	void setFluidAutoOutputEnabled(boolean enabled);

	boolean isFluidAutoOutputEnabled();

	void setFluidAutoOutputSide(Direction face);

	Direction getFluidAutoOutputSide();
}
