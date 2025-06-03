package conductance.core;

import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.util.Tuple;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.IOverclockable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.MachineRecipeProviderConfigAdapter;
import conductance.api.machine.recipe.RecipeCapabilityHolder;
import conductance.api.machine.recipe.RecipeModifier;
import conductance.api.util.overclock.Overclock;
import conductance.api.util.overclock.OverclockResult;
import conductance.api.tier.TierHolder;

public final class RecipeModifiers {

	public static final Function<Overclock, BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe>> ELECTRIC_OVERCLOCK = Util.memoize(overclock -> (machine, recipe) -> {
		if (machine instanceof TierHolder tieredMachine && CAPI.tiers().getTierByVoltage(recipe.getEnergyPerTick()).getIndex() > tieredMachine.getTier().getIndex()) {
			return null;
		}
		if (machine instanceof IOverclockable overclockable) {
			return RecipeModifiers.applyOverclock(overclock, recipe, overclockable.getOverclockVoltage());
		}
		return recipe;
	});

	public static Tuple<IRecipe, Integer> parallels(final MachineBlockEntity<?> machine, final IRecipe recipe, int maxParallels, final boolean modifyProcessTime) {
		if (machine instanceof MachineRecipeProviderConfigAdapter && machine instanceof final RecipeCapabilityHolder capabilityHolder) {
			while (maxParallels > 0) {
				final IRecipe copy = recipe.copy(RecipeModifier.multiply(maxParallels), modifyProcessTime);
				if (CAPI.recipeHelper().test(copy, capabilityHolder)) {
					return new Tuple<>(copy, maxParallels);
				}
				maxParallels /= 2;
			}
		}
		return new Tuple<>(recipe, 1);
	}

	public static IRecipe applyOverclock(final Overclock logic, IRecipe recipe, final long maxOverclockVoltage) {
		long recipeEnergyPerTick = recipe.getInputsPerTick(NCRecipeElementTypes.ENERGY).stream().mapToLong(element -> (long) element.data()).sum();
		if (recipeEnergyPerTick > 0) {
			final OverclockResult overclockResult = RecipeModifiers.performOverclocking(logic, recipe, recipeEnergyPerTick, maxOverclockVoltage);
			if (overclockResult.newEnergy() != recipeEnergyPerTick || recipe.getProcessTime() != overclockResult.newTime()) {
				recipe = recipe.copy(overclockResult, false);
			}
		}
		recipeEnergyPerTick = recipe.getOutputsPerTick(NCRecipeElementTypes.ENERGY).stream().mapToLong(element -> (long) element.data()).sum();
		if (recipeEnergyPerTick > 0) {
			final OverclockResult overclockResult = RecipeModifiers.performOverclocking(logic, recipe, recipeEnergyPerTick, maxOverclockVoltage);
			if (overclockResult.newEnergy() != recipeEnergyPerTick || recipe.getProcessTime() != overclockResult.newTime()) {
				recipe = recipe.copy(overclockResult, true);
			}
		}
		return recipe;
	}

	private static OverclockResult performOverclocking(final Overclock logic, @Nonnull final IRecipe recipe, final long recipeEnergy, final long maxOverclockVoltage) {
		final int recipeTier = CAPI.tiers().getTierByVoltage(recipeEnergy).getIndex();
		final int maximumTier = CAPI.tiers().getTierByVoltage(maxOverclockVoltage).getIndex();
		final int numberOfOCs = maximumTier - recipeTier;
		if (numberOfOCs <= 0) {
			return new OverclockResult(recipeEnergy, recipe.getProcessTime());
		}
		return logic.getOverclocker().apply(recipe, recipeEnergy, maxOverclockVoltage, recipe.getProcessTime(), numberOfOCs);
	}

	@Nullable
	public static Tuple<IRecipe, Integer> accurateParallel(final MachineBlockEntity<?> machine, @Nonnull final IRecipe recipe, final int maxParallel, final boolean modifyDuration) {
		if (maxParallel == 1) {
			return new Tuple<>(recipe, 1);
		}
		if (machine instanceof final RecipeCapabilityHolder holder) {
			final var parallel = RecipeModifiers.tryParallel(holder, recipe, 1, maxParallel, modifyDuration);
			return parallel == null ? new Tuple<>(recipe, 1) : parallel;
		}
		return null;
	}

	@Nullable
	private static Tuple<IRecipe, Integer> tryParallel(final RecipeCapabilityHolder holder, final IRecipe original, final int min, final int max, final boolean modifyDuration) {
		if (min > max) {
			return null;
		}
		final int mid = (min + max) / 2;
		final IRecipe copied = original.copy(RecipeModifier.multiply(mid), modifyDuration);
		if (!CAPI.recipeHelper().test(copied, holder) || !CAPI.recipeHelper().testPerTick(copied, holder)) {
			return RecipeModifiers.tryParallel(holder, original, min, mid - 1, modifyDuration);
		} else {
			if (mid == max) {
				return new Tuple<>(copied, mid);
			}
			final var tryMore = RecipeModifiers.tryParallel(holder, original, mid + 1, max, modifyDuration);
			return tryMore != null ? tryMore : new Tuple<>(copied, mid);
		}
	}

	private RecipeModifiers() {
	}
}
