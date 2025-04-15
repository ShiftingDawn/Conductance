package conductance.api.machine;

import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.util.RotationState;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> recipeType(NCRecipeType recipeType, NCRecipeType... moreTypes);

	MachineBuilder<T> recipeOutputLimits(Object2IntMap<IRecipeElementType<?>> recipeOutputLimits);

	MachineBuilder<T> recipeModifier(BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> modifier);

	MachineBuilder<T> rotationState(RotationState rotationState);

	MachineBuilder<T> guiSupplier(MachineGuiSupplier guiSupplier);

	MachineType<T> build();
}
