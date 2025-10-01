package conductance.api.util;

import java.util.Locale;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public enum ExtruderShape {

	ROD,
	BOLT,
	GEAR,
	GEAR_SMALL("small_gear"),
	RING,
	ROTOR,
	SCREW;

	private final String name;

	ExtruderShape(@Nullable final String name) {
		this.name = Objects.requireNonNullElseGet(name, () -> super.toString().toLowerCase(Locale.ROOT));
	}

	ExtruderShape() {
		this(null);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
