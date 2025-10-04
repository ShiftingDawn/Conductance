package conductance.api.machine;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import lombok.Getter;

public class MachineBlockItem<T extends MachineBlockEntity<T>> extends BlockItem {

	private final @Getter MachineType<T> machineType;

	public MachineBlockItem(final MachineBlock<T> machineBlock, final Properties properties) {
		super(machineBlock, properties.component(DataComponents.ITEM_NAME, machineBlock.getMachineType().getName()));
		this.machineType = machineBlock.getMachineType();
	}
}
