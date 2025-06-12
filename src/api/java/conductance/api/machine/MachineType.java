package conductance.api.machine;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.recipe.IRecipe;
import conductance.api.recipe.IRecipeElementType;
import conductance.api.recipe.NCRecipeType;
import conductance.api.registry.IRegistryObject;

public interface MachineType<T extends MachineBlockEntity<T>> extends IRegistryObject<String> {

	NCRecipeType[] getRecipeTypes();

	Map<IRecipeElementType<?>, Integer> getRecipeOutputLimits();

	BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> getRecipeModifier();

	NonNullSupplier<? extends MachineBlock<T>> getBlock();

	NonNullSupplier<BlockEntityType<T>> getBlockEntityType();

	@Nullable
	MachineGuiSupplier getGuiSupplier();

	@Nullable
	String getLocalizedName();

	BiConsumer<ItemStack, List<Component>> getTooltipBuilder();

	default BlockState getDefaultBlockState() {
		return this.getBlock().get().defaultBlockState();
	}
}
