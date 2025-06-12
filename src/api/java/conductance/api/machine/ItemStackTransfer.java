package conductance.api.machine;

import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.sync.ContentChangeListener;

public class ItemStackTransfer extends ItemStackHandler implements ContentChangeListener {

	@Getter
	@Setter
	@Nullable
	private Runnable contentChangeListener = null;
	@Getter
	@Setter
	private Predicate<ItemStack> filter = stack -> true;

	public ItemStackTransfer(final int size) {
		super(size);
	}

	public ItemStackTransfer(final NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	public ItemStackTransfer() {
	}

	@Override
	public boolean isItemValid(final int slot, final ItemStack stack) {
		return this.filter.test(stack);
	}

	@Override
	public void onContentsChanged(final int slot) {
		if (this.contentChangeListener != null) {
			this.contentChangeListener.run();
		}
	}

	public ItemStackTransfer copy() {
		final NonNullList<ItemStack> copiedStacks = NonNullList.withSize(this.stacks.size(), ItemStack.EMPTY);
		for (int i = 0; i < this.stacks.size(); ++i) {
			copiedStacks.set(i, this.stacks.get(i).copy());
		}
		final ItemStackTransfer copied = new ItemStackTransfer(copiedStacks);
		copied.setFilter(this.filter);
		return copied;
	}
}
