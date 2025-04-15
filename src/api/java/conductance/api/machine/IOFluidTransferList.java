package conductance.api.machine;

import java.util.List;
import java.util.function.Predicate;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import com.lowdragmc.lowdraglib.misc.FluidTransferList;
import lombok.Getter;
import conductance.api.util.IOMode;

public class IOFluidTransferList extends FluidTransferList {

	@Getter
	private final IOMode io;

	public IOFluidTransferList(final List<IFluidHandler> handlers, final IOMode io, final Predicate<FluidStack> filter) {
		super(handlers);
		this.io = io;
		this.setFilter(filter);
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (!this.io.isInput()) {
			return 0;
		}
		return super.fill(resource, action);
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (!this.io.isOutput()) {
			return FluidStack.EMPTY;
		}
		return super.drain(resource, action);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		if (!this.io.isOutput()) {
			return FluidStack.EMPTY;
		}
		return super.drain(maxDrain, action);
	}

	@Override
	public boolean supportsFill(final int tank) {
		return this.io.isInput();
	}

	@Override
	public boolean supportsDrain(final int tank) {
		return this.io.isOutput();
	}
}
