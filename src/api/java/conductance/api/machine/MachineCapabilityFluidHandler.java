package conductance.api.machine;

import java.util.function.BiConsumer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.util.GuiUtils;

public class MachineCapabilityFluidHandler extends MachineCapability implements IBlockCapabilityHandler, IDelegatedFluidHandler {

	private final @Getter MachineFluidHandler handler;
	private @Setter CapIO ioMode = CapIO.BOTH;

	public MachineCapabilityFluidHandler(final String key, final MachineBlockEntity<?> machine, final MachineFluidHandler handler) {
		super(key, machine);
		this.handler = handler;
		this.handler.setChangeListener(this::onContentsChanged);
	}

	@Override
	public CapIO getCapabilityIoMode() {
		return this.ioMode;
	}

	@Override
	public IFluidHandlerModifiable getRealFluidHandler() {
		return this.handler;
	}

	@Override
	public void serialize(final ValueOutput output) {
		this.handler.serialize(output);
	}

	@Override
	public void deserialize(final ValueInput input) {
		this.handler.deserialize(input);
	}

	public void onContentsChanged() {
		this.setChanged();
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (!this.canCapabilityInput()) {
			return 0;
		}
		return this.handler.fill(resource, action);
	}

	public int fillInternal(final FluidStack resource, final FluidAction action) {
		return this.handler.fill(resource, action);
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (!this.canCapabilityOutput()) {
			return FluidStack.EMPTY;
		}
		return this.handler.drain(resource, action);
	}

	public FluidStack drainInternal(final FluidStack resource, final FluidAction action) {
		return this.handler.drain(resource, action);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		if (!this.canCapabilityOutput()) {
			return FluidStack.EMPTY;
		}
		return this.handler.drain(maxDrain, action);
	}

	public FluidStack drainInternal(final int maxDrain, final FluidAction action) {
		return this.handler.drain(maxDrain, action);
	}

	@Override
	public void addGuiControls(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		if (this.canCapabilityInput()) {
			adder.accept("fluid_allow_overflow", CAPI.make(new ToggleButtonWidget(
				0, 0, 0, 0,
				new ManagedBoolean(this.handler::setAllowOverflow, this.handler::isAllowOverflow),
				toggled -> toggled ? GuiTextures.TEXTURE_FLUID_OVERFLOW_ON.get() : GuiTextures.TEXTURE_FLUID_OVERFLOW_OFF.get(),
				null
			), button -> button.addTooltipCallback((widget, tooltip) -> {
				final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.allow_overflow.fluid.disable" : "guiWidget.conductance.allow_overflow.fluid.enable");
			})));
		}
	}
}
