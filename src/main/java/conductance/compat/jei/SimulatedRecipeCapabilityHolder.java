package conductance.compat.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.recipe.MachineRecipe;
import conductance.api.recipe.RecipeElement;
import conductance.api.recipe.RecipeElementType;

final class SimulatedRecipeCapabilityHolder {

	private final @Getter List<Tuple<List<ItemStack>, RecipeElement>> inputItems;
	private final @Getter List<Tuple<List<ItemStack>, RecipeElement>> outputItems;
	private final @Getter List<Tuple<List<FluidStack>, RecipeElement>> inputFluids;
	private final @Getter List<Tuple<List<FluidStack>, RecipeElement>> outputFluids;
	private final @Getter List<Tuple<List<ItemStack>, RecipeElement>> perTickInputItems;
	private final @Getter List<Tuple<List<ItemStack>, RecipeElement>> perTickOutputItems;
	private final @Getter List<Tuple<List<FluidStack>, RecipeElement>> perTickInputFluids;
	private final @Getter List<Tuple<List<FluidStack>, RecipeElement>> perTickOutputFluids;

	SimulatedRecipeCapabilityHolder(final MachineRecipe recipe) {
		this.inputItems = this.makeItemList(recipe.getInputs());
		this.outputItems = this.makeItemList(recipe.getOutputs());
		this.inputFluids = this.makeFluidList(recipe.getInputs());
		this.outputFluids = this.makeFluidList(recipe.getOutputs());
		this.perTickInputItems = this.makeItemList(recipe.getPerTickInputs());
		this.perTickOutputItems = this.makeItemList(recipe.getPerTickOutputs());
		this.perTickInputFluids = this.makeFluidList(recipe.getPerTickInputs());
		this.perTickOutputFluids = this.makeFluidList(recipe.getPerTickOutputs());
	}

	private List<Tuple<List<ItemStack>, RecipeElement>> makeItemList(final Map<RecipeElementType<?>, List<RecipeElement>> rootMap) {
		return CAPI.make(new ArrayList<>(), items -> {
			final List<RecipeElement> inputs = rootMap.get(NCRecipeElementTypes.ITEM);
			if (inputs != null && !inputs.isEmpty()) {
				for (final RecipeElement input : inputs) {
					items.add(new Tuple<>(Util.make(new ArrayList<>(), list -> {
						final SizedIngredient ingredient = (SizedIngredient) input.data();
						ingredient.ingredient().getValues().forEach(item -> list.add(new ItemStack(item.value(), ingredient.count())));
					}), input));
				}
			}
		});
	}

	private List<Tuple<List<FluidStack>, RecipeElement>> makeFluidList(final Map<RecipeElementType<?>, List<RecipeElement>> rootMap) {
		return CAPI.make(new ArrayList<>(), fluids -> {
			final List<RecipeElement> inputs = rootMap.get(NCRecipeElementTypes.FLUID);
			if (inputs != null && !inputs.isEmpty()) {
				for (final RecipeElement input : inputs) {
					fluids.add(new Tuple<>(Util.make(new ArrayList<>(), list -> {
						final SizedFluidIngredient ingredient = (SizedFluidIngredient) input.data();
						ingredient.ingredient().fluids().forEach(fluid -> list.add(new FluidStack(fluid.value(), ingredient.amount())));
					}), input));
				}
			}
		});
	}
}
