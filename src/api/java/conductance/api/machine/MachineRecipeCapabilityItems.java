package conductance.api.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCRecipeElementTypes;
import conductance.api.recipe.MachineRecipe;
import conductance.api.util.IO;

public final class MachineRecipeCapabilityItems extends MachineRecipeCapability<SizedIngredient> implements IBlockCapabilityHandler, IItemHandlerModifiable {

	private final @Getter MachineInventory inventory;

	public MachineRecipeCapabilityItems(final MachineBlockEntity<?> machine, final int slots, final IO recipeIoMode, final CapIO capabilityIoMode, final IntFunction<MachineInventory> inventoryFactory) {
		super(machine, NCRecipeElementTypes.ITEM, recipeIoMode, capabilityIoMode);
		this.inventory = inventoryFactory.apply(slots);
		this.inventory.setChangeListener(this::setChanged);
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		this.inventory.serialize(valueOutput);
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		this.inventory.deserialize(valueInput);
	}

	@Override
	protected @Nullable List<SizedIngredient> handleInternal(final IO io, final MachineRecipe recipe, final List<SizedIngredient> inputs, final boolean simulate) {
		if (io != this.getRecipeIoMode()) {
			return inputs;
		}
		return switch (io) {
			case IN -> {
				final MachineInventory inv = simulate ? this.inventory.copy() : this.inventory;
				final List<SizedIngredient> leftOvers = new ArrayList<>();
				for (final SizedIngredient ingredient : inputs) {
					int ingredientCount = ingredient.count();
					for (int slot = 0; slot < inv.getSlots(); ++slot) {
						if (!ingredient.test(inv.getStackInSlot(slot))) {
							continue;
						}
						final ItemStack extracted = inv.extractItem(slot, ingredientCount, false);
						if (!extracted.isEmpty()) {
							ingredientCount -= extracted.getCount();
						}
						if (ingredientCount < 0) {
							break;
						}
					}
					if (ingredientCount > 0) {
						leftOvers.add(new SizedIngredient(ingredient.ingredient(), ingredientCount));
					}
				}
				yield !leftOvers.isEmpty() ? leftOvers : null;
			}
			case OUT -> {
				final MachineInventory inv = simulate ? this.inventory.copy() : this.inventory;
				final List<SizedIngredient> leftOvers = new ArrayList<>();
				for (final SizedIngredient ingredient : inputs) {
					ItemStack stackToInsert = new ItemStack(ingredient.ingredient().getValues().get(0).value(), ingredient.count());
					for (int slot = 0; slot < inv.getSlots(); ++slot) {
						stackToInsert = inv.insertItem(slot, stackToInsert, false);
						if (stackToInsert.isEmpty()) {
							break;
						}
					}
					if (!stackToInsert.isEmpty()) {
						leftOvers.add(new SizedIngredient(ingredient.ingredient(), stackToInsert.getCount()));
					}
				}
				yield !leftOvers.isEmpty() ? leftOvers : null;
			}
		};
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
	public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
		if (this.canCapabilityInput()) {
			return this.inventory.insertItem(slot, stack, simulate);
		}
		return stack;
	}

	@Override
	public ItemStack extractItem(final int slot, final int maxAmount, final boolean simulate) {
		if (this.canCapabilityOutput()) {
			return this.inventory.extractItem(slot, maxAmount, simulate);
		}
		return ItemStack.EMPTY;
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
