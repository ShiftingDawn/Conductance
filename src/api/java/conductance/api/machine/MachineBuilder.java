package conductance.api.machine;

import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.NCRecipeType;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> recipeType(NCRecipeType recipeType, NCRecipeType... moreTypes);

	MachineBuilder<T> guiSupplier(MachineGuiSupplier guiSupplier);

	MachineType<T> build();
}
