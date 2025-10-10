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
import conductance.api.machine.gui.GuiDrawableTexture;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.IWidgetContainer;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.ManagedInt;
import conductance.api.machine.gui.MutableSize;
import conductance.api.machine.gui.SimpleAutomaticGuiSetup;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.machine.gui.WidgetGroup;
import conductance.api.machine.gui.WidgetLayouts;
import conductance.api.util.Lazy;
import conductance.Conductance;

@RequiredArgsConstructor
public class GenericRecipeMachineGuiSetup extends SimpleAutomaticGuiSetup {

	private static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_ITEM_OFF = Lazy.of(() -> new GuiDrawableTexture(Conductance.id("conductance/item_auto_output_off")));
	private static final Lazy<GuiDrawableTexture> TEXTURE_AUTO_ITEM_ON = Lazy.of(() -> new GuiDrawableTexture(Conductance.id("conductance/item_auto_output_on")));

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
		final WidgetGroup buttonGroup = new WidgetGroup(3, 3, 0, 0);
		buttonGroup.setLayout(new WidgetLayouts.VerticalList(18, 18, 2, true));
		final GenericRecipeMachine machine = (GenericRecipeMachine) menu.getMachine();
		if (machine.getItemAutoOutput() != null) {
			buttonGroup.addWidget("item_auto_out", new ToggleButtonWidget(
				0, 0, 0, 0,
				new ManagedBoolean(toggled -> machine.getItemAutoOutput().setItemAutoOutputEnabled(toggled), () -> machine.getItemAutoOutput().isItemAutoOutputEnabled()),
				toggled -> toggled ? GenericRecipeMachineGuiSetup.TEXTURE_AUTO_ITEM_ON.get() : GenericRecipeMachineGuiSetup.TEXTURE_AUTO_ITEM_OFF.get()
			));
		}
		if (!buttonGroup.getWidgets().isEmpty()) {
			adder.accept("control_container", CAPI.make(new WidgetGroup(-22, 0, 22, buttonGroup.getHeight() + 4), group -> {
				group.setBackground(this.getTheme().getSidePanel());
				group.setSize(MutableSize.of(new ManagedInt(22), new ManagedInt(null, () -> buttonGroup.getHeight() + 6)));
				group.addWidget("controls", buttonGroup);
			}));
		}
	}

	@Override
	public GuiTheme getTheme() {
		return this.theme;
	}
}
