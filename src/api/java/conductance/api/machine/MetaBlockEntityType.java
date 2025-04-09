package conductance.api.machine;

import net.minecraft.world.level.block.entity.BlockEntityType;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.registry.IRegistryObject;

public interface MetaBlockEntityType<T extends MetaBlockEntity<T>> extends IRegistryObject<String> {

	NCRecipeType[] getRecipeTypes();

	NonNullSupplier<? extends MetaBlockEntityBlock<T>> getBlock();

	NonNullSupplier<BlockEntityType<T>> getBlockEntityType();

	@Nullable
	MachineGuiSupplier getGuiSupplier();
}
