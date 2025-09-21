package conductance.api.machine;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import lombok.Getter;

public class MachineBlock<T extends MachineBlockEntity<T>> extends Block {

	private final @Getter MachineType<T> machineType;

	public MachineBlock(final BlockBehaviour.Properties properties, final MachineType<T> machineType) {
		super(properties);
		this.machineType = machineType;
	}
}
