package conductance.api.machine;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.util.world.RotationState;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> blockFactory(MachineBlockFactory<T> blockFactory);

	MachineBuilder<T> itemFactory(MachineBlockItemFactory<T> itemFactory);

	MachineBuilder<T> blockEntityFactory(MachineBlockEntityFactory<T> blockEntityFactory);

	MachineBuilder<T> recipeType(NCRecipeType recipeType, NCRecipeType... moreTypes);

	MachineBuilder<T> recipeOutputLimits(Object2IntMap<IRecipeElementType<?>> recipeOutputLimits);

	MachineBuilder<T> recipeModifier(BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> modifier);

	MachineBuilder<T> rotationState(RotationState rotationState);

	MachineBuilder<T> modelRenderer(Supplier<IRenderer> modelRenderer);

	MachineBuilder<T> defaultModelRenderer(ResourceLocation baseModelLocation, @Nullable ResourceLocation overlayModelLocation);

	default MachineBuilder<T> defaultModelRenderer(final ResourceLocation baseModelLocation) {
		return this.defaultModelRenderer(baseModelLocation, null);
	}

	MachineBuilder<T> tieredModelRenderer(ResourceLocation baseModelLocation, String baseMachineKey, @Nullable ResourceLocation overlayModelLocation);

	default MachineBuilder<T> tieredModelRenderer(final ResourceLocation baseModelLocation, final String baseMachineKey) {
		return this.tieredModelRenderer(baseModelLocation, baseMachineKey, null);
	}

	MachineBuilder<T> workableModelRenderer(ResourceLocation baseModelLocation);

	MachineBuilder<T> tieredWorkableModelRenderer(ResourceLocation baseModelLocation, String baseMachineKey);

	MachineBuilder<T> guiSupplier(MachineGuiSupplier guiSupplier);

	MachineBuilder<T> tooltip(Component... tooltipLines);

	MachineBuilder<T> tooltip(BiConsumer<ItemStack, List<Component>> tooltipBuilder);

	MachineBuilder<T> localized(String localizedName);
}
