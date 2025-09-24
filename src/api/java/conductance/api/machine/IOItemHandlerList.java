package conductance.api.machine;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import lombok.Getter;
import lombok.Setter;

public class IOItemHandlerList implements IItemHandlerModifiable, ValueIOSerializable {

	private final IItemHandlerModifiable[] handlers;
	private final @Getter CapIO io;
	private @Setter Predicate<ItemStack> filter = item -> true;

	public IOItemHandlerList(final IItemHandlerModifiable[] handlers, final CapIO io) {
		this.handlers = handlers;
		this.io = io;
	}

	public IOItemHandlerList(final List<IItemHandlerModifiable> handlers, final CapIO io) {
		this(handlers.toArray(IItemHandlerModifiable[]::new), io);
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		final ValueOutput.ValueOutputList list = valueOutput.childrenList("handlers");
		for (int i = 0; i < this.handlers.length; ++i) {
			final ValueOutput entry = list.addChild();
			entry.putInt("i", i);
			if (this.handlers[i] instanceof final ValueIOSerializable serializable) {
				serializable.serialize(entry.child("data"));
			}
		}
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		valueInput.childrenList("handlers").ifPresent(list -> {
			for (final ValueInput entry : list) {
				final int i = entry.getIntOr("i", -1);
				if (i < 0 || i >= this.handlers.length) {
					continue;
				}
				if (this.handlers[i] instanceof final ValueIOSerializable serializable) {
					entry.child("data").ifPresent(serializable::deserialize);
				}
			}
		});
	}

	@Override
	public void setStackInSlot(final int slot, final ItemStack stack) {
		int index = 0;
		for (final IItemHandlerModifiable handler : this.handlers) {
			if (slot - index < handler.getSlots()) {
				handler.setStackInSlot(slot - index, stack);
				return;
			}
			index += handler.getSlots();
		}
	}

	@Override
	public int getSlots() {
		int slots = 0;
		for (final IItemHandlerModifiable handler : this.handlers) {
			slots += handler.getSlots();
		}
		return slots;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		int index = 0;
		for (final IItemHandlerModifiable handler : this.handlers) {
			if (slot - index < handler.getSlots()) {
				return handler.getStackInSlot(slot - index);
			}
			index += handler.getSlots();
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
		if (!this.io.isInput() || !this.filter.test(stack)) {
			return stack;
		}
		int index = 0;
		for (final IItemHandlerModifiable handler : this.handlers) {
			if (slot - index < handler.getSlots()) {
				return handler.insertItem(slot - index, stack, simulate);
			}
			index += handler.getSlots();
		}
		return stack;
	}

	@Override
	public ItemStack extractItem(final int slot, final int count, final boolean simulate) {
		if (!this.io.isOutput()) {
			return ItemStack.EMPTY;
		}
		int index = 0;
		for (final IItemHandlerModifiable handler : this.handlers) {
			if (slot - index < handler.getSlots()) {
				return handler.extractItem(slot - index, count, simulate);
			}
			index += handler.getSlots();
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(final int slot) {
		int index = 0;
		for (final IItemHandlerModifiable handler : this.handlers) {
			if (slot - index < handler.getSlots()) {
				return handler.getSlotLimit(slot - index);
			}
			index += handler.getSlots();
		}
		return 0;
	}

	@Override
	public boolean isItemValid(final int slot, final ItemStack stack) {
		if (!this.filter.test(stack)) {
			return false;
		}
		int index = 0;
		for (final IItemHandlerModifiable handler : this.handlers) {
			if (slot - index < handler.getSlots()) {
				return handler.isItemValid(slot - index, stack);
			}
			index += handler.getSlots();
		}
		return false;
	}
}
