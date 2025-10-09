package conductance.init.machine.boiler;

import java.util.function.BiConsumer;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSortedSets;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.machine.RecipePerTickFailureAction;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;

interface BoilerFakeRecipeCapabilityHolder extends RecipeCapabilityHolder {

	@Nullable MachineRecipeCapability<SizedFluidIngredient> getWaterTank();

	@Nullable MachineRecipeCapability<SizedFluidIngredient> getSteamTank();

	int getFuelForInput();

	void addInputs(BiConsumer<RecipeElementType<?>, Object> consumer);

	@Override
	default MachineRecipeType getRecipeType() {
		return null;
	}

	@Override
	default IntSortedSet getRecipePrograms() {
		return IntSortedSets.EMPTY_SET;
	}

	@Override
	default RecipePerTickFailureAction getPerTickFailureAction() {
		return RecipePerTickFailureAction.NOTHING;
	}
}
