package conductance.api.util.world;

import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public enum RotationState implements Predicate<Direction> {
	ALL(dir -> true, Direction.NORTH, BlockStateProperties.FACING),
	NONE(dir -> false, Direction.NORTH, DirectionProperty.create("facing", Direction.NORTH)),
	VERTICAL(dir -> dir.getAxis() == Direction.Axis.Y, Direction.UP, DirectionProperty.create("facing", Direction.Plane.VERTICAL)),
	HORIZONTAL(dir -> dir.getAxis() != Direction.Axis.Y, Direction.NORTH, BlockStateProperties.HORIZONTAL_FACING);

	private static final ThreadLocal<RotationState> STATE = new ThreadLocal<>();
	private final Predicate<Direction> predicate;
	public final Direction defaultDirection;
	public final DirectionProperty property;

	RotationState(final Predicate<Direction> predicate, final Direction defaultDirection, final DirectionProperty property) {
		this.predicate = predicate;
		this.defaultDirection = defaultDirection;
		this.property = property;
	}

	@Override
	public boolean test(final Direction dir) {
		return this.predicate.test(dir);
	}

	public static RotationState get() {
		return RotationState.STATE.get();
	}

	public static void set(final RotationState state) {
		RotationState.STATE.set(state);
	}

	public static void clear() {
		RotationState.STATE.remove();
	}
}
