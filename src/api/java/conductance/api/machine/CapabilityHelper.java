package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public final class CapabilityHelper {

	public static void tryTransferInventory(final IItemHandler source, final Level level, final BlockPos destPos, final @Nullable Direction side) {
		final IItemHandler dest = level.getCapability(Capabilities.ItemHandler.BLOCK, destPos, side);
		if (dest == null) {
			return;
		}
		for (int i = 0; i < source.getSlots(); ++i) {
			ItemStack stack = source.extractItem(i, Integer.MAX_VALUE, true);
			if (stack.isEmpty()) {
				continue;
			}
			final ItemStack leftOver = ItemHandlerHelper.insertItemStacked(dest, stack, true);
			final int inserted = stack.getCount() - leftOver.getCount();
			if (inserted > 0) {
				stack = source.extractItem(i, inserted, false);
				ItemHandlerHelper.insertItemStacked(dest, stack, false);
			}
		}
	}

	public static void tryTransferFluids(final IFluidHandler source, final Level level, final BlockPos destPos, final @Nullable Direction side) {
		final IFluidHandler dest = level.getCapability(Capabilities.FluidHandler.BLOCK, destPos, side);
		if (dest == null) {
			return;
		}
		FluidStack stack = source.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
		if (stack.isEmpty()) {
			return;
		}
		final int accepted = dest.fill(stack, IFluidHandler.FluidAction.SIMULATE);
		if (accepted > 0) {
			stack = source.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
			dest.fill(stack.copyWithAmount(accepted), IFluidHandler.FluidAction.EXECUTE);
		}
	}

	private CapabilityHelper() {
	}
}
