package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface MachineBlockEntityFactory<T extends MetaBlockEntity<T>, R extends BlockEntity & IMbeHolder<T>> {

	R newInstance(BlockPos pos, BlockState state, MetaBlockEntityType<T> metaBlockEntityType);
}
