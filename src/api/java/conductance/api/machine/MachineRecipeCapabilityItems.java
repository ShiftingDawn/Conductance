package conductance.api.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.recipe.MachineRecipe;
import conductance.api.util.GuiUtils;
import conductance.api.util.IO;

public final class MachineRecipeCapabilityItems extends MachineRecipeCapability<SizedIngredient> implements IBlockCapabilityHandler, IDelegatedItemHandler {

	private final @Getter MachineInventory inventory;

	public MachineRecipeCapabilityItems(final MachineBlockEntity<?> machine, final int slots, final IO recipeIoMode, final CapIO capabilityIoMode, final IntFunction<MachineInventory> inventoryFactory) {
		super(machine, NCRecipeElementTypes.ITEM, recipeIoMode, capabilityIoMode);
		this.inventory = inventoryFactory.apply(slots);
		this.inventory.setChangeListener(this::setChanged);
		this.addChangedListener(machine::syncToClient);
	}

	@Override
	public IItemHandlerModifiable getRealItemHandler() {
		return this.inventory;
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
	public List<SizedIngredient> getAvailableContent() {
		final ArrayList<SizedIngredient> result = new ArrayList<>();
		for (int i = 0; i < this.inventory.getSlots(); ++i) {
			final ItemStack stack = this.inventory.getStackInSlot(i);
			if (!stack.isEmpty()) {
				result.add(SizedIngredient.of(stack.getItem(), stack.getCount()));
			}
		}
		return Collections.unmodifiableList(result);
	}

	@Override
	public int getMaxSpaceForContent(final SizedIngredient object) {
		if (object.ingredient().getValues().size() == 0) {
			return 0;
		}
		final Item item = object.ingredient().getValues().get(0).value();
		final ItemStack leftOver = ItemHandlerHelper.insertItem(this.inventory, new ItemStack(item, object.count()), true);
		return leftOver.isEmpty() ? object.count() : object.count() - leftOver.getCount();
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
	public void addGuiControls(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		if (this.canCapabilityInput()) {
			adder.accept("item_allow_overflow", CAPI.make(new ToggleButtonWidget(
				0, 0, 0, 0,
				new ManagedBoolean(this.inventory::setAllowOverflow, this.inventory::isAllowOverflow),
				toggled -> toggled ? GuiTextures.TEXTURE_ITEM_OVERFLOW_ON.get() : GuiTextures.TEXTURE_ITEM_OVERFLOW_OFF.get(),
				null
			), button -> button.addTooltipCallback((widget, tooltip) -> {
				final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.allow_overflow.item.disable" : "guiWidget.conductance.allow_overflow.item.enable");
			})));
		}
	}
}
