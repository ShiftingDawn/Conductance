package conductance.api.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.Direction;

public final class ModelUtils {

	public static final Map<Direction, String> LOGICAL_SIDES;

	static {
		LOGICAL_SIDES = Collections.unmodifiableMap(Util.make(new EnumMap<>(Direction.class), map -> {
			map.put(Direction.UP, "top");
			map.put(Direction.DOWN, "bottom");
			map.put(Direction.NORTH, "front");
			map.put(Direction.EAST, "side");
			map.put(Direction.SOUTH, "back");
			map.put(Direction.WEST, "side");
		}));
	}

	private ModelUtils() {
	}
}
