package conductance.api.machine;

import net.minecraft.core.Direction;

public interface IItemAutoOutput {

	void setItemAutoOutputEnabled(boolean enabled);

	boolean isItemAutoOutputEnabled();

	void setItemAutoOutputSide(Direction face);

	Direction getItemAutoOutputSide();
}
