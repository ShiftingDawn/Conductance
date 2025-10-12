package conductance.api.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import conductance.api.CAPI;
import conductance.api.NCDataComponents;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.tier.Tier;
import conductance.api.util.IO;

public final class RecipeHelper {

	public boolean test(final MachineRecipe recipe, final RecipeCapabilityHolder holder) {
		if (recipe.getProgram() != -1) {
			if (!holder.getRecipePrograms().contains(recipe.getProgram())) {
				return false;
			}
		}
		final boolean ins = this.testInternal(recipe, holder, IO.IN, recipe.getInputs());
		final boolean outs = this.testInternal(recipe, holder, IO.OUT, recipe.getOutputs());
		return ins & outs;
	}

	public boolean testPerTick(final MachineRecipe recipe, final RecipeCapabilityHolder holder) {
		final boolean ins = this.testInternal(recipe, holder, IO.IN, recipe.getPerTickInputs());
		final boolean outs = this.testInternal(recipe, holder, IO.OUT, recipe.getPerTickOutputs());
		return ins & outs;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private boolean testInternal(final MachineRecipe recipe, final RecipeCapabilityHolder holder, final IO io, final Map<RecipeElementType<?>, List<RecipeElement>> map) {
		for (final Map.Entry<RecipeElementType<?>, List<RecipeElement>> entry : map.entrySet()) {
			final List<MachineRecipeCapability<?>> handlers = holder.getRecipeCapabilities(entry.getKey(), io);
			if (handlers.isEmpty() && !entry.getValue().isEmpty()) {
				return false;
			}
			List recipeElementContentList = entry.getValue().stream()
				.map(RecipeElement::data)
				.toList();
			for (final MachineRecipeCapability handler : handlers) {
				recipeElementContentList = handler.handle(io, recipe, recipeElementContentList, true);
				if (recipeElementContentList == null || recipeElementContentList.isEmpty()) {
					break;
				}
			}
			if (recipeElementContentList != null && !recipeElementContentList.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void handle(final MachineRecipe recipe, final IO io, final RecipeCapabilityHolder holder) {
		this.handleInternal(recipe, holder, io, switch (io) {
			case IN -> recipe.getInputs();
			case OUT -> recipe.getOutputs();
		});
	}

	public void handlePerTick(final MachineRecipe recipe, final IO io, final RecipeCapabilityHolder holder) {
		this.handleInternal(recipe, holder, io, switch (io) {
			case IN -> recipe.getPerTickInputs();
			case OUT -> recipe.getPerTickOutputs();
		});
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void handleInternal(final MachineRecipe recipe, final RecipeCapabilityHolder holder, final IO io, final Map<RecipeElementType<?>, List<RecipeElement>> map) {
		for (final Map.Entry<RecipeElementType<?>, List<RecipeElement>> entry : map.entrySet()) {
			final List<MachineRecipeCapability<?>> handlers = holder.getRecipeCapabilities(entry.getKey(), io);
			if (handlers.isEmpty() && !entry.getValue().isEmpty()) {
				continue;
			}
			List recipeElementContentList = entry.getValue().stream()
				.filter(RecipeElement::testChance)
				.map(RecipeElement::data)
				.toList();
			for (final MachineRecipeCapability handler : handlers) {
				recipeElementContentList = handler.handle(io, recipe, recipeElementContentList, false);
				if (recipeElementContentList == null || recipeElementContentList.isEmpty()) {
					break;
				}
			}
		}
	}

	public static IntSortedSet findPrograms(final IItemHandler handler) {
		return Util.make(new IntLinkedOpenHashSet(), set -> {
			for (int i = 0; i < handler.getSlots(); ++i) {
				final ItemStack stack = handler.getStackInSlot(i);
				final int program = stack.isEmpty() ? -1 : stack.getOrDefault(NCDataComponents.PROGRAM_CIRCUIT, -1);
				if (program >= 0) {
					set.add(program);
				}
			}
		});
	}

	public static AutoRecipeData calc(final Material material, final MaterialGenerationHandler input, final MaterialGenerationHandler output, final int processTime, final long baseEnergy) {
		final long inputValue = CAPI.materials().getUnitValue(material, input);
		final long outputValue = CAPI.materials().getUnitValue(material, output);
		if (inputValue == outputValue) {
			return new AutoRecipeData(1, 1, processTime, baseEnergy);
		} else if (inputValue < outputValue) {
			final int diffAmount = (int) (outputValue / inputValue);
			return new AutoRecipeData(diffAmount, 1, processTime * diffAmount, diffAmount * baseEnergy);
		} else {
			final int diffAmount = (int) (inputValue / outputValue);
			return new AutoRecipeData(1, diffAmount, processTime * diffAmount, diffAmount * baseEnergy);
		}
	}

	public static AutoRecipeData calc(final Material material, final MaterialGenerationHandler input, final MaterialGenerationHandler output, final int processTime, final Tier energyTier) {
		return RecipeHelper.calc(material, input, output, processTime, energyTier.getRecipeVoltage());
	}

	public static void calc(final Material material, final MaterialGenerationHandler input, final MaterialGenerationHandler output, final int processTime, final long baseEnergy, final AutoRecipeDataCallback callback) {
		final AutoRecipeData data = RecipeHelper.calc(material, input, output, processTime, baseEnergy);
		callback.accept(data.inAmount(), data.outAmount(), data.processTime(), data.totalEnergy());
	}

	public static void calc(final Material material, final MaterialGenerationHandler input, final MaterialGenerationHandler output, final int processTime, final Tier energyTier, final AutoRecipeDataCallback callback) {
		RecipeHelper.calc(material, input, output, processTime, energyTier.getRecipeVoltage(), callback);
	}
}
