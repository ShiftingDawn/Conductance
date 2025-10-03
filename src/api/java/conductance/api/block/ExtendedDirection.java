package conductance.api.block;

import java.util.Locale;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Rotation;
import lombok.Getter;

public enum ExtendedDirection implements StringRepresentable {

	NORTH_UP(Direction.NORTH, Rotation.NONE),
	NORTH_DOWN(Direction.NORTH, Rotation.CLOCKWISE_180),
	NORTH_LEFT(Direction.NORTH, Rotation.COUNTERCLOCKWISE_90),
	NORTH_RIGHT(Direction.NORTH, Rotation.CLOCKWISE_90);

	private final String name = super.toString().toLowerCase(Locale.ROOT);
	private final @Getter Direction facing;
	private final @Getter Rotation rotation;

	ExtendedDirection(final Direction facing, final Rotation rotation) {
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
}
