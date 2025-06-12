package conductance.lib.pipenet;

import java.util.Arrays;
import java.util.List;
import net.minecraft.core.Direction;

public final class PipeNetHelper {

	public static final int ITEM_CONNECTIONS = 0b001100; //North and south

	public static boolean isConnected(final int connections, final Direction dir) {
		return !PipeNetHelper.isBlocked(connections, dir);
	}

	public static boolean isBlocked(final int connections, final Direction dir) {
		return (connections & 1 << dir.ordinal()) == 0;
	}

	public static int setConnection(final int connections, final Direction dir, final boolean connected) {
		if (connected) {
			return connections | 1 << dir.ordinal();
		} else {
			return connections & ~(1 << dir.ordinal());
		}
	}

	public static List<Direction> getConnections(final int connections) {
		return Arrays.stream(Direction.values()).filter(dir -> PipeNetHelper.isConnected(connections, dir)).toList();
	}

	private PipeNetHelper() {
	}
}
