package conductance.api.machine;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.recipe.IRecipe;
import conductance.api.recipe.IRecipeElementType;
import conductance.api.recipe.NCRecipeType;
import conductance.api.util.world.RotationState;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> blockFactory(MachineBlockFactory<T> blockFactory);

	MachineBuilder<T> itemFactory(MachineBlockItemFactory<T> itemFactory);

	MachineBuilder<T> blockEntityFactory(MachineBlockEntityFactory<T> blockEntityFactory);

	MachineBuilder<T> recipeType(NCRecipeType recipeType, NCRecipeType... moreTypes);

	MachineBuilder<T> recipeOutputLimits(Object2IntMap<IRecipeElementType<?>> recipeOutputLimits);

	MachineBuilder<T> recipeModifier(BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> modifier);

	MachineBuilder<T> rotationState(RotationState rotationState);

	<A> MachineBuilder<T> modelType(MachineModelType<A> type, A data);

	MachineBuilder<T> modelType(MachineModelType<Void> type);

	MachineBuilder<T> guiSupplier(MachineGuiSupplier guiSupplier);

	MachineBuilder<T> tooltip(Component... tooltipLines);

	MachineBuilder<T> tooltip(BiConsumer<ItemStack, List<Component>> tooltipBuilder);

	MachineBuilder<T> localized(String localizedName);
}
