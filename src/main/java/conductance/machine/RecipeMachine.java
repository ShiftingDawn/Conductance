package conductance.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityType;

public class RecipeMachine extends MetaBlockEntity<RecipeMachine> {

	public RecipeMachine(final MetaBlockEntityType<RecipeMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}
}
