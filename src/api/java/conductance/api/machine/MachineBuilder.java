package conductance.api.machine;

import java.util.function.BiFunction;
import net.minecraft.resources.ResourceLocation;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.util.RotationState;

public interface MachineBuilder<T extends MachineBlockEntity<T>> {

	MachineBuilder<T> blockFactory(MachineBlockFactory<T> blockFactory);

	MachineBuilder<T> itemFactory(MachineBlockItemFactory<T> itemFactory);

	MachineBuilder<T> blockEntityFactory(MachineBlockEntityFactory<T> blockEntityFactory);

	MachineBuilder<T> recipeType(NCRecipeType recipeType, NCRecipeType... moreTypes);

	MachineBuilder<T> recipeOutputLimits(Object2IntMap<IRecipeElementType<?>> recipeOutputLimits);

	MachineBuilder<T> recipeModifier(BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> modifier);

	MachineBuilder<T> rotationState(RotationState rotationState);

	MachineBuilder<T> modelRenderer(IRenderer modelRenderer);

	MachineBuilder<T> defaultModelRenderer(ResourceLocation baseModelLocation, @Nullable ResourceLocation overlayModelLocation);

	default MachineBuilder<T> defaultModelRenderer(final ResourceLocation baseModelLocation) {
		return this.defaultModelRenderer(baseModelLocation, null);
	}

	MachineBuilder<T> workableModelRenderer(ResourceLocation baseModelLocation);

	MachineBuilder<T> guiSupplier(MachineGuiSupplier guiSupplier);

	MachineType<T> build();
}
