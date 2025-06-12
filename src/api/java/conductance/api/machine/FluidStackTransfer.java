package conductance.api.machine;

import java.util.function.Predicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import com.lowdragmc.lowdraglib.side.fluid.IFluidHandlerModifiable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.sync.ContentChangeListener;

public class FluidStackTransfer extends FluidTank implements ContentChangeListener, INBTSerializable<CompoundTag>, IFluidHandlerModifiable {

	@Getter
	@Setter
	@Nullable
	private Runnable contentChangeListener = null;

	public FluidStackTransfer(final int capacity) {
		this(capacity, e -> true);
	}

	public FluidStackTransfer(final int capacity, final Predicate<FluidStack> filter) {
		super(capacity, filter);
	}

	public FluidStackTransfer(final FluidStack stack) {
		super(stack.getAmount());
		this.setFluid(stack);
	}

	@Override
	protected void onContentsChanged() {
		if (this.contentChangeListener != null) {
			this.contentChangeListener.run();
		}
	}

	@Override
	public void setFluidInTank(final int tank, final FluidStack stack) {
		this.setFluid(stack);
		this.onContentsChanged();
	}

	@Override
	public CompoundTag serializeNBT(final HolderLookup.Provider provider) {
		return this.writeToNBT(provider, new CompoundTag());
	}

	@Override
	public void deserializeNBT(final HolderLookup.Provider provider, final CompoundTag nbt) {
		this.readFromNBT(provider, nbt);
	}

	public FluidStackTransfer copy() {
		final FluidStack copiedStack = this.fluid.copy();
		final FluidStackTransfer copied = new FluidStackTransfer(this.capacity, this.validator);
		copied.setFluid(copiedStack);
		return copied;
	}
}
