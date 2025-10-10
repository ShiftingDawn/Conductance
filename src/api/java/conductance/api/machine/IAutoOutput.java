package conductance.api.machine;

import net.minecraft.core.Direction;

public interface IAutoOutput extends IItemAutoOutput, IFluidAutoOutput {

	default boolean isAutoOutputEnabled() {
		return this.isItemAutoOutputEnabled() || this.isFluidAutoOutputEnabled();
	}

	default void setAutoOutputSide(final Direction face) {
		this.setItemAutoOutputSide(face);
		this.setFluidAutoOutputSide(face);
	}
}
