package conductance.api.machine;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import lombok.Getter;
import lombok.Setter;

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
}
