package conductance.api.machine.event;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeType;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> blockFactory(MachineBlockFactory<T> blockFactory);

	MachineBuilder<T> itemFactory(MachineBlockItemFactory<T> itemFactory);

	MachineBuilder<T> simpleModel(ResourceLocation casingTexture);

	MachineBuilder<T> recipeType(MachineRecipeType recipeType, MachineRecipeType... additionalRecipeTypes);

	MachineBuilder<T> guiSetup(@Nullable GuiSetup guiSetup);

	MachineBuilder<T> rotationType(BlockRotationType type);

	MachineBuilder<T> customModel();
}
