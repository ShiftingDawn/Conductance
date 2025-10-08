package conductance.init.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.machine.CapIO;
import conductance.api.machine.MachineCapabilityInventory;
import conductance.api.machine.MachineInventory;
import conductance.api.machine.MachineType;

public class SteamSolidFuelBoilerMachine extends AbstractSteamBoilerMachine<SteamSolidFuelBoilerMachine> {

	private final @Getter MachineCapabilityInventory inputItems;

	public SteamSolidFuelBoilerMachine(final MachineType<SteamSolidFuelBoilerMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.inputItems = new MachineCapabilityInventory("input", this, new MachineInventory(1));
		this.inputItems.setIoMode(CapIO.IN);
	}
}
