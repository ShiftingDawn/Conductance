package conductance.compat.jei;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import lombok.Getter;
import conductance.api.NCRecipeElementTypes;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.RecipeObject;

final class SimulatedRecipeCapabilityHolder {

	private final @Getter List<Tuple<List<ItemStack>, RecipeObject>> inputItems;
	private final @Getter List<Tuple<List<ItemStack>, RecipeObject>> outputItems;
	private final @Getter List<Tuple<List<FluidStack>, RecipeObject>> inputFluids;
	private final @Getter List<Tuple<List<FluidStack>, RecipeObject>> outputFluids;

	SimulatedRecipeCapabilityHolder(final MachineRecipe recipe) {
		this.inputItems = Util.make(new ArrayList<>(), items -> {
			final List<RecipeObject> inputs = recipe.getInputs().get(NCRecipeElementTypes.ITEM);
			if (inputs != null && !inputs.isEmpty()) {
				for (final RecipeObject input : inputs) {
					items.add(new Tuple<>(Util.make(new ArrayList<>(), list -> {
						final SizedIngredient ingredient = (SizedIngredient) input.data();
						ingredient.ingredient().getValues().forEach(item -> list.add(new ItemStack(item.value(), ingredient.count())));
					}), input));
				}
			}
		});
		this.outputItems = Util.make(new ArrayList<>(), items -> {
			final List<RecipeObject> inputs = recipe.getOutputs().get(NCRecipeElementTypes.ITEM);
			if (inputs != null && !inputs.isEmpty()) {
				for (final RecipeObject input : inputs) {
					items.add(new Tuple<>(Util.make(new ArrayList<>(), list -> {
						final SizedIngredient ingredient = (SizedIngredient) input.data();
						ingredient.ingredient().getValues().forEach(item -> list.add(new ItemStack(item.value(), ingredient.count())));
					}), input));
				}
			}
		});
		this.inputFluids = Util.make(new ArrayList<>(), items -> {
			final List<RecipeObject> inputs = recipe.getInputs().get(NCRecipeElementTypes.FLUID);
			if (inputs != null && !inputs.isEmpty()) {
				for (final RecipeObject input : inputs) {
					items.add(new Tuple<>(Util.make(new ArrayList<>(), list -> {
						final SizedFluidIngredient ingredient = (SizedFluidIngredient) input.data();
						ingredient.ingredient().fluids().forEach(fluid -> list.add(new FluidStack(fluid.value(), ingredient.amount())));
					}), input));
				}
			}
		});
		this.outputFluids = Util.make(new ArrayList<>(), items -> {
			final List<RecipeObject> inputs = recipe.getOutputs().get(NCRecipeElementTypes.FLUID);
			if (inputs != null && !inputs.isEmpty()) {
				for (final RecipeObject input : inputs) {
					items.add(new Tuple<>(Util.make(new ArrayList<>(), list -> {
						final SizedFluidIngredient ingredient = (SizedFluidIngredient) input.data();
						ingredient.ingredient().fluids().forEach(fluid -> list.add(new FluidStack(fluid.value(), ingredient.amount())));
					}), input));
				}
			}
		});
	}
}
