package conductance.core.machine;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import conductance.api.resource.RuntimeModelProvider;
import conductance.api.resource.BlockModelBuilder;
import conductance.api.resource.BlockStateBuilder;
import conductance.Conductance;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class DirectionalMachineRuntimeModelProvider implements RuntimeModelProvider {

	@Override
	public void createBlockState(final ResourceLocation blockId, final BlockStateBuilder builder, final ResourceLocation defaultModelLocation) {
		builder.variants(b -> {
			b.variant(HORIZONTAL_FACING, Direction.NORTH).model(defaultModelLocation);
			b.variant(HORIZONTAL_FACING, Direction.SOUTH).model(defaultModelLocation).y(180);
			b.variant(HORIZONTAL_FACING, Direction.EAST).model(defaultModelLocation).y(90);
			b.variant(HORIZONTAL_FACING, Direction.WEST).model(defaultModelLocation).y(270);
		});
	}

	@Override
	public void createBlockModel(final ResourceLocation blockId, final BlockModelBuilder<?> builder) {
		builder.parent(Conductance.id("block/cube_machine_overlay"));
	}
}
