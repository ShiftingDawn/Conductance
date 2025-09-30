package conductance.compat.jei;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.NCRecipeElementTypes;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.RecipeObject;

final class SimulatedRecipeCapabilityHolder {

	private final @Getter MachineRecipe recipe;
	private final @Getter ItemHandler inputItems;
	private final @Getter ItemHandler outputItems;

	SimulatedRecipeCapabilityHolder(final MachineRecipe recipe) {
		this.recipe = recipe;
		this.inputItems = Util.make(() -> {
			final List<RecipeObject> inputs = recipe.getInputs().get(NCRecipeElementTypes.ITEM);
			if (inputs == null || inputs.isEmpty()) {
				return new ItemHandler(List.of());
			}
			final List<List<ItemStack>> items = new ArrayList<>();
			for (final RecipeObject input : inputs) {
				items.add(Util.make(new ArrayList<>(), list -> {
					final SizedIngredient ingredient = (SizedIngredient) input.data();
					ingredient.ingredient().getValues().forEach(item -> list.add(new ItemStack(item.value(), ingredient.count())));
				}));
			}
			return new ItemHandler(items);
		});
		this.outputItems = Util.make(() -> {
			final List<RecipeObject> inputs = recipe.getOutputs().get(NCRecipeElementTypes.ITEM);
			if (inputs == null || inputs.isEmpty()) {
				return new ItemHandler(List.of());
			}
			final List<List<ItemStack>> items = new ArrayList<>();
			for (final RecipeObject input : inputs) {
				items.add(Util.make(new ArrayList<>(), list -> {
					final SizedIngredient ingredient = (SizedIngredient) input.data();
					ingredient.ingredient().getValues().forEach(item -> list.add(new ItemStack(item.value(), ingredient.count())));
				}));
			}
			return new ItemHandler(items);
		});
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static final class ItemHandler implements IItemHandler {

		@Getter
		private final List<List<ItemStack>> stacks;

		@Override
		public int getSlots() {
			return this.stacks.size();
		}

		@Override
		public ItemStack getStackInSlot(final int slot) {
			final List<ItemStack> stackList = this.stacks.get(slot);
			if (stackList == null || stackList.isEmpty()) {
				return ItemStack.EMPTY;
			}
			return stackList.get(Math.abs((int) (System.currentTimeMillis() / 1000) % stackList.size()));
		}

		@Override
		public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
			return stack;
		}

		@Override
		public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(final int slot) {
			return 64;
		}

		@Override
		public boolean isItemValid(final int slot, final ItemStack stack) {
			return true;
		}
	}
}
