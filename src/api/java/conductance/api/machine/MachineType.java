package conductance.api.machine;

import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.registry.IRegistryObject;

public interface MachineType<T extends MachineBlockEntity<T>> extends IRegistryObject<String> {

	NCRecipeType[] getRecipeTypes();

	Map<IRecipeElementType<?>, Integer> getRecipeOutputLimits();

	BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> getRecipeModifier();

	NonNullSupplier<? extends MachineBlock<T>> getBlock();

	NonNullSupplier<BlockEntityType<T>> getBlockEntityType();

	IRenderer getModelRenderer();

	@Nullable
	MachineGuiSupplier getGuiSupplier();

	default BlockState getDefaultBlockState() {
		return this.getBlock().get().defaultBlockState();
	}
}
