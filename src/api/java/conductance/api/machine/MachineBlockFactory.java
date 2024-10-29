package conductance.api.machine;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface MachineBlockFactory {

	Block newInstance(BlockBehaviour.Properties properties, MetaBlockEntityType<?> metaBlockEntityType);
}
