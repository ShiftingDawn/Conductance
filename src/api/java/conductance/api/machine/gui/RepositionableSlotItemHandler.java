package conductance.api.machine.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import lombok.Getter;
import conductance.api.machine.CapIO;

/**
 * @see net.neoforged.neoforge.items.SlotItemHandler
 */
public class RepositionableSlotItemHandler extends RepositionableSlot {

	private static final Container EMPTY_INVENTORY = new SimpleContainer(0);
	private final @Getter IItemHandler itemHandler;

	public RepositionableSlotItemHandler(final IItemHandler itemHandler, final int slot, final int initialX, final int initialY, final CapIO io) {
		super(RepositionableSlotItemHandler.EMPTY_INVENTORY, slot, initialX, initialY, io);
		this.itemHandler = itemHandler;
	}

	@Override
	public boolean mayPlace(final ItemStack stack) {
		if (!this.getIo().isInput() || stack.isEmpty()) {
			return false;
		}
		return this.itemHandler.isItemValid(this.getContainerSlot(), stack);
	}

	@Override
	public ItemStack getItem() {
		return this.getItemHandler().getStackInSlot(this.getContainerSlot());
	}

	@Override
	public void set(final ItemStack stack) {
		((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(this.getContainerSlot(), stack);
		this.setChanged();
	}

	@Override
	public void onQuickCraft(final ItemStack oldStackIn, final ItemStack newStackIn) {
	}

	@Override
	public int getMaxStackSize() {
		return this.itemHandler.getSlotLimit(this.getContainerSlot());
	}

	@Override
	public int getMaxStackSize(final ItemStack stack) {
		return Math.min(stack.getMaxStackSize(), this.itemHandler.getSlotLimit(this.getContainerSlot()));
	}

	@Override
	public boolean mayPickup(final Player playerIn) {
		return this.getIo().isOutput() && !this.getItemHandler().extractItem(this.getContainerSlot(), 1, true).isEmpty();
	}

	@Override
	public ItemStack remove(final int amount) {
		return this.getItemHandler().extractItem(this.getContainerSlot(), amount, false);
	}

	@Override
	public boolean isSameInventory(final Slot other) {
		if (other instanceof final RepositionableSlotItemHandler slot) {
			return this.itemHandler == slot.itemHandler;
		} else if (other instanceof final SlotItemHandler slot) {
			return this.itemHandler == slot.getItemHandler();
		}
		return false;
	}
}
