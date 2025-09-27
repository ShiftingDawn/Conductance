package conductance.api.machine;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class MachineCapabilityInventory extends MachineCapability implements IBlockCapabilityHandler, IItemHandlerModifiable {

	public final MachineInventory inventory;

	public MachineCapabilityInventory(final String key, final MachineBlockEntity<?> machine, final MachineInventory inventory) {
		super(key, machine);
		this.inventory = inventory;
		this.inventory.setChangeListener(this::onContentsChanged);
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		this.inventory.serialize(valueOutput);
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		this.inventory.deserialize(valueInput);
	}

	public void onContentsChanged() {
		this.setChanged();
	}

	@Override
	public CapIO getCapabilityIoMode() {
		return CapIO.BOTH;
	}

	@Override
	public void setStackInSlot(final int slot, final ItemStack stack) {
		this.inventory.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return this.inventory.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(final int item, final ItemStack stack, final boolean simulate) {
		if (!this.canCapabilityInput()) {
			return stack;
		}
		return this.insertItemInternal(item, stack, simulate);
	}

	public ItemStack insertItemInternal(final int slot, final ItemStack stack, final boolean simulate) {
		return this.inventory.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(final int slot, final int count, final boolean simulate) {
		if (!this.canCapabilityOutput()) {
			return ItemStack.EMPTY;
		}
		return this.extractItemInternal(slot, count, simulate);
	}

	public ItemStack extractItemInternal(final int slot, final int count, final boolean simulate) {
		return this.inventory.extractItem(slot, count, simulate);
	}

	@Override
	public int getSlotLimit(final int slot) {
		return this.inventory.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(final int slot, final ItemStack stack) {
		return this.inventory.isItemValid(slot, stack);
	}
}
