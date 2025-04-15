package conductance.api.machine.capability;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.ICapabilityHandler;
import conductance.api.machine.ItemStackTransfer;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.util.IOMode;

public class MachineCapabilityInventory extends MachineCapability implements ICapabilityHandler, IItemHandlerModifiable {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineCapabilityInventory.class, MachineCapability.MANAGED_FIELD_HOLDER);
	@Persisted
	@DescSynced
	public final ItemStackTransfer inventory;
	@Nullable
	private Boolean isEmpty;

	public MachineCapabilityInventory(final MachineBlockEntity<?> machineBlockEntity, final int slots) {
		super(machineBlockEntity);
		this.inventory = new ItemStackTransfer(slots);
		this.inventory.setOnContentsChanged(this::onContentsChanged);
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return MachineCapabilityInventory.MANAGED_FIELD_HOLDER;
	}

	public void onContentsChanged() {
		this.isEmpty = null;
		this.notifyListeners();
	}

	@NotNull
	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.inventory.getStackInSlot(slot);
	}

	@Override
	public void setStackInSlot(final int index, final ItemStack stack) {
		this.inventory.setStackInSlot(index, stack);
	}


	@NotNull
	@Override
	public ItemStack insertItem(final int slot, @NotNull final ItemStack stack, final boolean simulate) {
		if (this.canCapabilityInput()) {
			return this.inventory.insertItem(slot, stack, simulate);
		}
		return stack;
	}

	public ItemStack insertItemInternal(final int slot, @NotNull final ItemStack stack, final boolean simulate) {
		return this.inventory.insertItem(slot, stack, simulate);
	}

	@NotNull
	@Override
	public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
		if (this.canCapabilityOutput()) {
			return this.inventory.extractItem(slot, amount, simulate);
		}
		return ItemStack.EMPTY;
	}

	public ItemStack extractItemInternal(final int slot, final int amount, final boolean simulate) {
		return this.inventory.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlots() {
		return this.inventory.getSlots();
	}

	@Override
	public int getSlotLimit(final int slot) {
		return this.inventory.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(final int slot, @NotNull final ItemStack stack) {
		return this.inventory.isItemValid(slot, stack);
	}

	public boolean isEmpty() {
		if (this.isEmpty == null) {
			this.isEmpty = true;
			for (int i = 0; i < this.inventory.getSlots(); i++) {
				if (!this.inventory.getStackInSlot(i).isEmpty()) {
					this.isEmpty = false;
					break;
				}
			}
		}
		return this.isEmpty;
	}

	@Override
	public IOMode getHandlerIoMode() {
		return IOMode.INPUT_OUTPUT;
	}
}
