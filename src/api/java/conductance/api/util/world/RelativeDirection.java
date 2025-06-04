package conductance.api.util.world;

import java.util.Locale;
import net.minecraft.core.Direction;

public enum RelativeDirection {

	FRONT, BACK, TOP, BOTTOM, SIDE;

	private final String name = super.toString().toLowerCase(Locale.ROOT);

	@Override
	public String toString() {
		return this.name;
	}

	public static RelativeDirection byDirection(final Direction direction) {
		return switch (direction) {
			case NORTH -> RelativeDirection.FRONT;
			case SOUTH -> RelativeDirection.BACK;
			case UP -> RelativeDirection.TOP;
			case DOWN -> RelativeDirection.BOTTOM;
			default -> RelativeDirection.SIDE;
		};
	}
}
