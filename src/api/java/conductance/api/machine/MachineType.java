package conductance.api.machine;

import java.util.function.Supplier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface MachineType<T extends MachineBlockEntity<T>> {

	Supplier<MachineBlock<T>> getBlock();

	Supplier<MachineBlockItem<T>> getItem();

	Supplier<BlockEntityType<T>> getBlockEntityType();
}
