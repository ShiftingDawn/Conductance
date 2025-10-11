package conductance.api.machine.event;

import java.util.function.Function;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeType;

public interface AbstractMachineBuilder<T extends MachineBlockEntity<T>, BUILDER extends AbstractMachineBuilder<T, BUILDER>> {

	BUILDER blockFactory(MachineBlockFactory<T> blockFactory);

	BUILDER itemFactory(MachineBlockItemFactory<T> itemFactory);

	BUILDER recipeType(MachineRecipeType recipeType, MachineRecipeType... additionalRecipeTypes);

	BUILDER recipeModifier(MachineRecipeModifier modifier);

	BUILDER guiSetup(@Nullable GuiSetup guiSetup);

	BUILDER rotationType(BlockRotationType type);

	BUILDER simpleModel(ResourceLocation casingTexture);

	BUILDER sidedMachineModel(ResourceLocation textureBaseLocation);

	BUILDER customModel();

	BUILDER customName(Function<String, MutableComponent> nameFactory);
}
