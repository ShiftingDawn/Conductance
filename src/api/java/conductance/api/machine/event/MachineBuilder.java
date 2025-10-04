package conductance.api.machine.event;

import java.util.function.Function;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.tier.Tier;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> blockFactory(MachineBlockFactory<T> blockFactory);

	MachineBuilder<T> itemFactory(MachineBlockItemFactory<T> itemFactory);

	MachineBuilder<T> simpleModel(ResourceLocation casingTexture);

	MachineBuilder<T> tieredModel(String machineModelKey, Tier tier);

	MachineBuilder<T> recipeType(MachineRecipeType recipeType, MachineRecipeType... additionalRecipeTypes);

	MachineBuilder<T> guiSetup(@Nullable GuiSetup guiSetup);

	MachineBuilder<T> rotationType(BlockRotationType type);

	MachineBuilder<T> customModel();

	MachineBuilder<T> customName(Function<String, MutableComponent> nameFactory);
}
