package conductance.init.machine.boiler;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.IFluidHandlerModifiable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.SimpleAutomaticGuiSetup;

public abstract class AbstractSteamBoilerMachineGuiSetup extends SimpleAutomaticGuiSetup {

	@Override
	protected @Nullable IItemHandlerModifiable getOutputItems(final MachineBlockEntity<?> machine) {
		return null;
	}

	@Override
	protected @Nullable IFluidHandlerModifiable getInputFluids(final MachineBlockEntity<?> machine) {
		final AbstractSteamBoilerMachine<?> boiler = (AbstractSteamBoilerMachine<?>) machine;
		return boiler.getWaterTank().getRealFluidHandler();
	}

	@Override
	protected @Nullable IFluidHandlerModifiable getOutputFluids(final MachineBlockEntity<?> machine) {
		final AbstractSteamBoilerMachine<?> boiler = (AbstractSteamBoilerMachine<?>) machine;
		return boiler.getSteamTank().getRealFluidHandler();
	}

	@Override
	protected @Nullable IEnergyHandler getEnergyHandler(final MachineBlockEntity<?> machine) {
		return null;
	}

	@Override
	protected @Nullable RecipeHandler getRecipeHandler(final MachineBlockEntity<?> machine) {
		return null;
	}

	@Override
	public GuiTheme getTheme() {
		return GuiTheme.THEME_BRONZE;
	}
}
