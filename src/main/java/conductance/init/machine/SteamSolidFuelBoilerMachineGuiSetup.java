package conductance.init.machine;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import conductance.api.machine.MachineBlockEntity;

public final class SteamSolidFuelBoilerMachineGuiSetup extends AbstractSteamBoilerMachineGuiSetup {

	@Override
	protected IItemHandlerModifiable getInputItems(final MachineBlockEntity<?> machine) {
		final SteamSolidFuelBoilerMachine boiler = (SteamSolidFuelBoilerMachine) machine;
		return boiler.getInputItems().getInventory();
	}
}
