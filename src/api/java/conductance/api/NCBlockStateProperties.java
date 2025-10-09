package conductance.api;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import conductance.api.block.FacingAndRotation;

public final class NCBlockStateProperties {

	public static final EnumProperty<Direction> FACING_ALL = BlockStateProperties.FACING;
	public static final EnumProperty<Direction> FACING_HORIZONTAL = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<Direction> FACING_VERTICAL = BlockStateProperties.VERTICAL_DIRECTION;
	public static final EnumProperty<FacingAndRotation> FACING_EXTENDED = EnumProperty.create("facing", FacingAndRotation.class);

	public static final BooleanProperty WORKING = BooleanProperty.create("working");
	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	private NCBlockStateProperties() {
	}
}
