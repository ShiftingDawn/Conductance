package conductance.core.pipenet;

import net.minecraft.world.level.Level;

public final class PerTickLongHandler extends PerTickHandler<Long> {

	public PerTickLongHandler(final Long defaultValue) {
		super(defaultValue);
	}

	public void increment(final Level level, final long amount) {
		this.updateState(level);
		this.setCurrentValue(this.getCurrentValue() + amount);
	}
}
