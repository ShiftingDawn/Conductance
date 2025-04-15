package conductance.api.machine.capability;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.ICapabilityHandler;
import conductance.api.machine.ItemStackTransfer;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.util.IOMode;

public class MachineRecipeCapabilityItems extends MachineRecipeCapability<SizedIngredient> implements ICapabilityHandler, IItemHandlerModifiable {

	public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineRecipeCapabilityItems.class, MachineRecipeCapability.MANAGED_FIELD_HOLDER);
	@Persisted
	@DescSynced
	public final ItemStackTransfer inventory;
	@Nullable
	private Boolean isEmpty;

	public MachineRecipeCapabilityItems(final MachineBlockEntity<?> machine, final int slots, final IOMode capabilityIoMode, final IOMode handlerIoMode, final Function<Integer, ItemStackTransfer> inventoryFactory) {
		super(machine, NCRecipeElementTypes.ITEM, capabilityIoMode, handlerIoMode);
		this.inventory = inventoryFactory.apply(slots);
		this.inventory.setOnContentsChanged(this::onContentsChanged);
	}

	public MachineRecipeCapabilityItems(final MachineBlockEntity<?> machine, final int slots, final IOMode capabilityIoMode, final IOMode handlerIoMode) {
		this(machine, slots, capabilityIoMode, handlerIoMode, ItemStackTransfer::new);
	}

	public MachineRecipeCapabilityItems(final MachineBlockEntity<?> machine, final int slots, final IOMode ioMode) {
		this(machine, slots, ioMode, ioMode);
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return MachineRecipeCapabilityItems.MANAGED_FIELD_HOLDER;
	}

	public MachineRecipeCapabilityItems setFilter(final Predicate<ItemStack> filter) {
		this.inventory.setFilter(filter);
		return this;
	}

	public void exportToNearby(@NotNull final Direction... facings) {
		if (this.isEmpty()) {
			return;
		}
		final var level = this.getMachineBlockEntity().getLevel();
		assert level != null;
		final var pos = this.getMachineBlockEntity().getBlockPos();
		for (final Direction facing : facings) {
			ItemTransferHelper.exportToTarget(this, Integer.MAX_VALUE, f -> true, level, pos.relative(facing), facing.getOpposite());
		}
	}

	public void importFromNearby(@NotNull final Direction... facings) {
		final var level = this.getMachineBlockEntity().getLevel();
		assert level != null;
		final var pos = this.getMachineBlockEntity().getBlockPos();
		for (final Direction facing : facings) {
			ItemTransferHelper.importToTarget(this, Integer.MAX_VALUE, f -> true, level, pos.relative(facing), facing.getOpposite());
		}
	}

	@Override
	@Nullable
	protected List<SizedIngredient> handleInternal(final IOMode ioMode, final IRecipe recipe, final List<SizedIngredient> inputs, final boolean simulate) {
		if (ioMode != this.getCapabilityIoMode()) {
			return inputs;
		}
		final ItemStackTransfer inv = simulate ? this.inventory.copy() : this.inventory;
		final Iterator<SizedIngredient> iterator = inputs.iterator();
		if (ioMode == IOMode.INPUT) {
			while (iterator.hasNext()) {
				final SizedIngredient ingredient = iterator.next();
				SLOT_LOOKUP:
				for (int i = 0; i < inv.getSlots(); ++i) {
					final ItemStack itemStack = inv.getStackInSlot(i);
					if (ingredient.test(itemStack)) {
						final ItemStack[] ingredientStacks = ingredient.getItems();
						for (final ItemStack ingredientStack : ingredientStacks) {
							if (ingredientStack.is(itemStack.getItem())) {
								final ItemStack extracted = inv.extractItem(i, ingredientStack.getCount(), false);
								ingredientStack.setCount(ingredientStack.getCount() - extracted.getCount());
								if (ingredientStack.isEmpty()) {
									iterator.remove();
									break SLOT_LOOKUP;
								}
							}
						}
					}
				}
			}
		} else if (ioMode == IOMode.OUTPUT) {
			while (iterator.hasNext()) {
				final SizedIngredient ingredient = iterator.next();
				final var items = ingredient.getItems();
				if (items.length == 0) {
					iterator.remove();
					continue;
				}
				final ItemStack output = items[0];
				if (!output.isEmpty()) {
					for (int i = 0; i < inv.getSlots(); ++i) {
						final ItemStack leftStack = inv.insertItem(i, output.copy(), false);
						output.setCount(leftStack.getCount());
						if (output.isEmpty()) {
							break;
						}
					}
				}
				if (output.isEmpty()) {
					iterator.remove();
				}
			}
		}
		return inputs.isEmpty() ? null : inputs;
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
}
