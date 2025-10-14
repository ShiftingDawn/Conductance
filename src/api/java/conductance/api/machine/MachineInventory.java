package conductance.api.machine;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public class MachineInventory extends ItemStackHandler implements IChangeAware {

	@Getter
	@Setter
	@Nullable
	private Runnable changeListener = null;
	@Getter
	@Setter
	private InventoryPredicate filter = (slot, stack) -> true;
	@Nullable
	private Boolean isEmpty;
	@Getter
	private boolean allowOverflow = true;

	public MachineInventory() {
	}

	public MachineInventory(final int size) {
		super(size);
	}

	public MachineInventory(final NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	@Override
	public void serialize(final ValueOutput output) {
		super.serialize(output);
		output.putBoolean("overflow", this.allowOverflow);
	}

	@Override
	public void deserialize(final ValueInput input) {
		super.deserialize(input);
		this.allowOverflow = input.getBooleanOr("overflow", this.allowOverflow);
	}

	@Override
	public boolean isItemValid(final int slot, final ItemStack stack) {
		return this.filter.test(slot, stack);
	}

	@Override
	protected void onContentsChanged(final int slot) {
		this.isEmpty = null;
		if (this.changeListener != null) {
			this.changeListener.run();
		}
	}

	@Override
	public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
		if (!this.allowOverflow) {
			for (int i = 0; i < this.getSlots(); ++i) {
				if (ItemStack.isSameItemSameComponents(this.getStackInSlot(i), stack)) {
					if (slot != i) {
						return stack;
					}
				}
			}
		}
		return super.insertItem(slot, stack, simulate);
	}

	public void setAllowOverflow(final boolean allowOverflow) {
		this.allowOverflow = allowOverflow;
		if (this.changeListener != null) {
			this.changeListener.run();
		}
	}

	public boolean isEmpty() {
		if (this.isEmpty == null) {
			this.isEmpty = true;
			for (int i = 0; i < this.getSlots(); ++i) {
				if (!this.getStackInSlot(i).isEmpty()) {
					this.isEmpty = false;
					break;
				}
			}
		}
		return this.isEmpty;
	}

	/**
	 * Creates a deep copy of this inventory and filter. Change listeners and overflow state are NOT copied.
	 *
	 * @return an identical copy
	 */
	public MachineInventory copy() {
		final NonNullList<ItemStack> copiedStacks = NonNullList.withSize(this.stacks.size(), ItemStack.EMPTY);
		for (int i = 0; i < this.stacks.size(); ++i) {
			copiedStacks.set(i, this.stacks.get(i).copy());
		}
		final MachineInventory copied = new MachineInventory(copiedStacks);
		copied.setFilter(this.filter);
		return copied;
	}
}
