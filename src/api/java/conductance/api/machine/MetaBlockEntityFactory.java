package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface MetaBlockEntityFactory<T extends MetaBlockEntity<T>> {

	T newInstance(MetaBlockEntityType<T> type, BlockPos pos, BlockState blockState);
}
