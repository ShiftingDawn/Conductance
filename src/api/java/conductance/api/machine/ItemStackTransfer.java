package conductance.api.machine;

import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import lombok.Getter;
import lombok.Setter;

public class ItemStackTransfer extends ItemStackHandler implements IContentChangeAware {

	@Getter
	@Setter
	private Runnable onContentsChanged = () -> {
	};
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
		this.onContentsChanged.run();
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
