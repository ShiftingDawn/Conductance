package conductance.api.capability.cover;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public interface ICoverable {

	CoverManager getCoverManager();

	Direction getFrontFacing();

	static VoxelShape getCoverBackplateShape(final Direction side, final double plateThickness) {
		return switch (side) {
			case UP -> Shapes.box(0.0, 1.0 - plateThickness, 0.0, 1.0, 1.0, 1.0);
			case DOWN -> Shapes.box(0.0, 0.0, 0.0, 1.0, plateThickness, 1.0);
			case NORTH -> Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, plateThickness);
			case SOUTH -> Shapes.box(0.0, 0.0, 1.0 - plateThickness, 1.0, 1.0, 1.0);
			case WEST -> Shapes.box(0.0, 0.0, 0.0, plateThickness, 1.0, 1.0);
			case EAST -> Shapes.box(1.0 - plateThickness, 0.0, 0.0, 1.0, 1.0, 1.0);
		};
	}

	static boolean doesCoverCollide(@Nullable final Direction side, final List<VoxelShape> collisionBox, final double plateThickness) {
		if (side != null && plateThickness > 0.0) {
			final VoxelShape coverPlateBox = ICoverable.getCoverBackplateShape(side, plateThickness);
			for (final AABB aabb : coverPlateBox.toAabbs()) {
				if (Shapes.collide(side.getAxis(), aabb, collisionBox, plateThickness) < plateThickness) {
					return true;
				}
			}
		}
		return false;
	}
}
