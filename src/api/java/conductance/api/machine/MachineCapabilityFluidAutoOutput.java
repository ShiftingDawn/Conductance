package conductance.api.machine;

import java.util.function.BiConsumer;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.util.GuiUtils;

public class MachineCapabilityFluidAutoOutput extends MachineCapability implements IFluidAutoOutput {

	private final MachineFluidHandler handler;
	private boolean enabled = false;
	private Direction side = Direction.NORTH;
	private @Nullable MachineTick tick = null;

	public MachineCapabilityFluidAutoOutput(final String key, final MachineBlockEntity<?> machine, final MachineFluidHandler handler) {
		super(key, machine);
		this.handler = handler;
		this.handler.addChangeListener(this::revalidateTick);
		this.addChangedListener(this::revalidateTick);
	}

	protected void revalidateTick() {
		if (!this.getOwner().isServerSide()) {
			return;
		}
		this.tick = this.getOwner().addTick(this::tick, this.tick);
	}

	private void tick() {
		assert this.tick != null;
		if (this.handler.isEmpty() || !this.isFluidAutoOutputEnabled()) {
			this.tick.invalidate();
			return;
		}
		if (this.getOwner().haveTicksPassed(10)) {
			CapabilityHelper.tryExportFluids(this.handler, this.getOwner().getLevel(), this.getOwner().getBlockPos().relative(this.side), this.side.getOpposite());
		}
	}

	@Override
	public void serialize(final ValueOutput output) {
		output.putBoolean("enabled", this.enabled);
		output.store("side", Direction.CODEC, this.side);
	}

	@Override
	public void deserialize(final ValueInput input) {
		this.enabled = input.getBooleanOr("enabled", false);
		this.side = input.read("side", Direction.CODEC).orElse(Direction.NORTH);
	}

	@Override
	public void setFluidAutoOutputEnabled(final boolean enable) {
		this.enabled = enable;
		this.setChanged();
	}

	@Override
	public boolean isFluidAutoOutputEnabled() {
		return this.enabled;
	}

	@Override
	public void setFluidAutoOutputSide(final Direction face) {
		this.side = face;
		this.setChanged();
	}

	@Override
	public Direction getFluidAutoOutputSide() {
		return this.side;
	}

	@Override
	public void addModelData(final ModelData.Builder builder) {
		builder.with(MachineModelProperties.FLUID_AUTO_OUTPUT, new Tuple<>(this.side, this.isFluidAutoOutputEnabled()));
	}

	@Override
	public void addGuiControls(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		adder.accept("fluid_auto_out", CAPI.make(new ToggleButtonWidget(
			0, 0, 0, 0,
			new ManagedBoolean(this::setFluidAutoOutputEnabled, this::isFluidAutoOutputEnabled),
			toggled -> toggled ? GuiTextures.TEXTURE_AUTO_FLUID_OUTPUT_ON.get() : GuiTextures.TEXTURE_AUTO_FLUID_OUTPUT_OFF.get(),
			null
		), button -> button.addTooltipCallback((widget, tooltip) -> {
			final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
			GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.auto_output.fluid.disable" : "guiWidget.conductance.auto_output.fluid.enable");
		})));
	}
}
