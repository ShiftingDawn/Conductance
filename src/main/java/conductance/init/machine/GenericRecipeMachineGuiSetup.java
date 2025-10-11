package conductance.init.machine;

import java.util.function.BiConsumer;
import net.minecraft.Util;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.IFluidHandlerModifiable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineRecipeCapabilityFluids;
import conductance.api.machine.MachineRecipeCapabilityItems;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.energy.IEnergyHandler;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.IWidgetContainer;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.SimpleAutomaticGuiSetup;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.util.GuiUtils;

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
			Util.make(new ShowRecipeViewerHandlers(menu.getMachine()), handler -> {
				final IGuiWidget widget = root.getWidgetById("progress");
				if (widget != null) {
					widget.addTooltipCallback(handler);
					widget.addMouseListener(handler);
				}
			});
		}
	}

	@Override
	public void addControlWidgets(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		final GenericRecipeMachine machine = (GenericRecipeMachine) menu.getMachine();
		if (machine.getItemAutoOutput() != null) {
			adder.accept("item_auto_out", CAPI.make(new ToggleButtonWidget(
				0, 0, 0, 0,
				new ManagedBoolean(toggled -> machine.getItemAutoOutput().setItemAutoOutputEnabled(toggled), () -> machine.getItemAutoOutput().isItemAutoOutputEnabled()),
				toggled -> toggled ? GuiTextures.TEXTURE_AUTO_ITEM_OUTPUT_ON.get() : GuiTextures.TEXTURE_AUTO_ITEM_OUTPUT_OFF.get(),
				null
			), button -> button.addTooltipCallback((widget, tooltip) -> {
				final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.auto_output.item.disable" : "guiWidget.conductance.auto_output.item.enable");
			})));
		}
		if (machine.getFluidAutoOutput() != null) {
			adder.accept("fluid_auto_out", CAPI.make(new ToggleButtonWidget(
				0, 0, 0, 0,
				new ManagedBoolean(toggled -> machine.getFluidAutoOutput().setFluidAutoOutputEnabled(toggled), () -> machine.getFluidAutoOutput().isFluidAutoOutputEnabled()),
				toggled -> toggled ? GuiTextures.TEXTURE_AUTO_FLUID_OUTPUT_ON.get() : GuiTextures.TEXTURE_AUTO_FLUID_OUTPUT_OFF.get(),
				null
			), button -> button.addTooltipCallback((widget, tooltip) -> {
				final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.auto_output.fluid.disable" : "guiWidget.conductance.auto_output.fluid.enable");
			})));
		}
	}

	@Override
	public GuiTheme getTheme() {
		return this.theme;
	}
}
