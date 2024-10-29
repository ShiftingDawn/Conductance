package conductance.api.machine;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface MachineBlockFactory<T extends MetaBlockEntity<T>> {

	MetaBlockEntityBlock<T> newInstance(BlockBehaviour.Properties properties, MetaBlockEntityType<T> metaBlockEntityType);
}
