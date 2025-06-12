package conductance.lib.pipenet;

import net.minecraft.world.level.Level;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public class PerTickHandler<T> {

	private final T defaultValue;
	private long lastTime;
	private T currentValue;

	public PerTickHandler(final T defaultValue) {
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
	}

	protected final void updateState(@Nullable final Level level) {
		if (level == null) {
			return;
		}
		final long curTime = level.getGameTime(); //Time in ticks
		if (curTime != this.lastTime) {
			this.currentValue = this.defaultValue;
			this.lastTime = curTime;
		}
	}

	public final void set(final Level level, final T value) {
		this.updateState(level);
		this.currentValue = value;
	}

	public final T get(final Level level) {
		this.updateState(level);
		return this.currentValue;
	}
}
