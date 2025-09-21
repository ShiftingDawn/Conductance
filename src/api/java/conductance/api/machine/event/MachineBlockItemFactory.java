package conductance.api.machine.event;

import net.minecraft.world.item.Item;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;

public interface MachineBlockItemFactory<T extends MachineBlockEntity<T>> {

	MachineBlockItem<T> apply(MachineBlock<T> block, Item.Properties properties);
}
