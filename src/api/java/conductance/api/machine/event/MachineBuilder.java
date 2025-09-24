package conductance.api.machine.event;

import net.minecraft.resources.ResourceLocation;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.recipe.MachineRecipeType;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> blockFactory(MachineBlockFactory<T> blockFactory);

	MachineBuilder<T> itemFactory(MachineBlockItemFactory<T> itemFactory);

	MachineBuilder<T> simpleModel(ResourceLocation casingTexture);

	MachineBuilder<T> recipeType(MachineRecipeType recipeType, MachineRecipeType... additionalRecipeTypes);

	MachineBuilder<T> customModel();
}
