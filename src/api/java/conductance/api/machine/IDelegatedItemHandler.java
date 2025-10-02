package conductance.api.machine;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public interface IDelegatedItemHandler extends IItemHandlerModifiable {

	IItemHandlerModifiable getRealItemHandler();

	@Override
	default void setStackInSlot(final int slot, final ItemStack stack) {
		this.getRealItemHandler().setStackInSlot(slot, stack);
	}

	@Override
	default int getSlots() {
		return this.getRealItemHandler().getSlots();
	}

	@Override
	default ItemStack getStackInSlot(final int slot) {
		return this.getRealItemHandler().getStackInSlot(slot);
	}

	@Override
	default ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
		return this.getRealItemHandler().insertItem(slot, stack, simulate);
	}

	@Override
	default ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
		return this.getRealItemHandler().extractItem(slot, amount, simulate);
	}

	@Override
	default int getSlotLimit(final int slot) {
		return this.getRealItemHandler().getSlotLimit(slot);
	}

	@Override
	default boolean isItemValid(final int slot, final ItemStack stack) {
		return this.getRealItemHandler().isItemValid(slot, stack);
	}
}
