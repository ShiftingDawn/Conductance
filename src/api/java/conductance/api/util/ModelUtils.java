package conductance.api.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.Direction;

public final class ModelUtils {

	public static final Map<Direction, IntPos> MODEL_ROTATION;
	public static final Map<Direction, String> LOGICAL_SIDES;

	static {
		MODEL_ROTATION = Collections.unmodifiableMap(Util.make(new EnumMap<>(Direction.class), map -> {
			map.put(Direction.UP, new IntPos(270, 0));
			map.put(Direction.DOWN, new IntPos(90, 0));
			map.put(Direction.NORTH, new IntPos(0, 0));
			map.put(Direction.EAST, new IntPos(0, 90));
			map.put(Direction.SOUTH, new IntPos(0, 180));
			map.put(Direction.WEST, new IntPos(0, 270));
		}));
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
