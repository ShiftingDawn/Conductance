package conductance.api.machine;

import net.minecraft.world.level.block.entity.BlockEntityType;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.registry.IRegistryObject;

public interface MetaBlockEntityType<T extends MetaBlockEntity<T>> extends IRegistryObject<String> {

	NCRecipeType[] getRecipeTypes();

	NonNullSupplier<? extends MetaBlockEntityBlock<T>> getBlock();

	NonNullSupplier<BlockEntityType<T>> getBlockEntityType();
}
