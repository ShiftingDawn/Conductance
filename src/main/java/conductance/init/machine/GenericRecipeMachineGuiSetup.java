package conductance.init.machine;

import java.util.Arrays;
import java.util.function.BiConsumer;
import net.minecraft.Util;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.IFluidHandlerModifiable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.IWidgetContainer;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.SimpleAutomaticGuiSetup;

@RequiredArgsConstructor
public class GenericRecipeMachineGuiSetup extends SimpleAutomaticGuiSetup {

	private final GuiTheme theme;

	public GenericRecipeMachineGuiSetup() {
		this(GuiTheme.THEME_DEFAULT);
	}

	@Override
	protected @Nullable IItemHandlerModifiable getInputItems(final MachineBlockEntity<?> machine) {
		final MachineRecipeCapabilityItems capability = ((GenericRecipeMachine) machine).getInputItems();
		return capability != null ? capability.getInventory() : null;
	}

	@Override
	protected @Nullable IItemHandlerModifiable getOutputItems(final MachineBlockEntity<?> machine) {
		final MachineRecipeCapabilityItems capability = ((GenericRecipeMachine) machine).getOutputItems();
		return capability != null ? capability.getRealItemHandler() : null;
	}

	@Override
	protected @Nullable IFluidHandlerModifiable getInputFluids(final MachineBlockEntity<?> machine) {
		final MachineRecipeCapabilityFluids capability = ((GenericRecipeMachine) machine).getInputFluids();
		return capability != null ? capability.getRealFluidHandler() : null;
	}

	@Override
	protected @Nullable IFluidHandlerModifiable getOutputFluids(final MachineBlockEntity<?> machine) {
		final MachineRecipeCapabilityFluids capability = ((GenericRecipeMachine) machine).getOutputFluids();
		return capability != null ? capability.getRealFluidHandler() : null;
	}

	@Override
	protected IEnergyHandler getEnergyHandler(final MachineBlockEntity<?> machine) {
		return ((GenericRecipeMachine) machine).getEnergy();
	}

	@Override
	protected @Nullable RecipeHandler getRecipeHandler(final MachineBlockEntity<?> machine) {
		return ((GenericRecipeMachine) machine).getRecipeHandler();
	}

	@Override
	public void addWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		super.addWidgets(menu, adder);
		if (menu.getWidgetById("root") instanceof final IWidgetContainer root) {
			final boolean hasNonHiddenRecipeTypes = Arrays.stream(menu.getMachine().getMachineType().getRecipeTypes()).anyMatch(type -> !type.isHidden());
			if (hasNonHiddenRecipeTypes) {
				Util.make(new ShowRecipeViewerHandlers(menu.getMachine()), handler -> {
					final IGuiWidget widget = root.getWidgetById("progress");
					if (widget != null) {
						widget.addTooltipCallback(handler);
						widget.addMouseListener(handler);
					}
				});
			}
		}
	}

	@Override
	public GuiTheme getTheme() {
		return this.theme;
	}
}
