package conductance.api.machine;

import net.minecraft.world.item.Item;

public interface MachineBlockItemFactory<T extends MachineBlockEntity<T>> {

	IMachineBlockItem<T> newInstance(IMachineBlock<T> block, Item.Properties properties);
}
