package conductance.api.block;

import java.util.Locale;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Rotation;
import lombok.Getter;

public enum FacingAndRotation implements StringRepresentable {

	DOWN_UP(Direction.DOWN, Rotation.NONE),
	DOWN_DOWN(Direction.DOWN, Rotation.CLOCKWISE_180),
	DOWN_LEFT(Direction.DOWN, Rotation.CLOCKWISE_90),
	DOWN_RIGHT(Direction.DOWN, Rotation.COUNTERCLOCKWISE_90),
	UP_UP(Direction.UP, Rotation.NONE),
	UP_DOWN(Direction.UP, Rotation.CLOCKWISE_180),
	UP_LEFT(Direction.UP, Rotation.CLOCKWISE_90),
	UP_RIGHT(Direction.UP, Rotation.COUNTERCLOCKWISE_90),
	NORTH_UP(Direction.NORTH, Rotation.NONE),
	NORTH_DOWN(Direction.NORTH, Rotation.CLOCKWISE_180),
	NORTH_LEFT(Direction.NORTH, Rotation.CLOCKWISE_90),
	NORTH_RIGHT(Direction.NORTH, Rotation.COUNTERCLOCKWISE_90),
	SOUTH_UP(Direction.SOUTH, Rotation.NONE),
	SOUTH_DOWN(Direction.SOUTH, Rotation.CLOCKWISE_180),
	SOUTH_LEFT(Direction.SOUTH, Rotation.CLOCKWISE_90),
	SOUTH_RIGHT(Direction.SOUTH, Rotation.COUNTERCLOCKWISE_90),
	WEST_UP(Direction.WEST, Rotation.NONE),
	WEST_DOWN(Direction.WEST, Rotation.CLOCKWISE_180),
	WEST_LEFT(Direction.WEST, Rotation.CLOCKWISE_90),
	WEST_RIGHT(Direction.WEST, Rotation.COUNTERCLOCKWISE_90),
	EAST_UP(Direction.EAST, Rotation.NONE),
	EAST_DOWN(Direction.EAST, Rotation.CLOCKWISE_180),
	EAST_LEFT(Direction.EAST, Rotation.CLOCKWISE_90),
	EAST_RIGHT(Direction.EAST, Rotation.COUNTERCLOCKWISE_90);

	private final String name = super.toString().toLowerCase(Locale.ROOT);
	private final @Getter Direction facing;
	private final @Getter Rotation rotation;

	FacingAndRotation(final Direction facing, final Rotation rotation) {
		this.facing = facing;
		this.rotation = rotation;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static FacingAndRotation get(final Direction facing, final Rotation rotation) {
		for (final FacingAndRotation direction : FacingAndRotation.values()) {
			if (direction.facing == facing && direction.rotation == rotation) {
				return direction;
			}
		}
		return FacingAndRotation.NORTH_UP;
	}
}
