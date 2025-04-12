package conductance.api.machine;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import com.lowdragmc.lowdraglib.misc.ItemTransferList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import conductance.api.util.IOMode;

public class IOItemTransferList extends ItemTransferList {

	@Getter
	private final IOMode io;

	public IOItemTransferList(final List<IItemHandlerModifiable> handlers, final IOMode io, final Predicate<ItemStack> filter) {
		super(handlers);
		this.io = io;
		this.setFilter(filter);
	}

	@Override
	@NotNull
	public ItemStack insertItem(final int slot, @NotNull final ItemStack stack, final boolean simulate) {
		if (!this.io.isInput()) {
			return stack;
		}
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	@NotNull
	public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
		if (!this.io.isOutput()) {
			return ItemStack.EMPTY;
		}
		return super.extractItem(slot, amount, simulate);
	}
}
