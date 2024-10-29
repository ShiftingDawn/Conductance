package conductance.api.machine;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import conductance.api.registry.IRegistryObject;

public interface MetaBlockEntityType<T extends MetaBlockEntity<T>> extends IRegistryObject<String> {

	NonNullSupplier<? extends Block> getBlock();

	NonNullSupplier<? extends BlockEntityType<?>> getBlockEntityType();

	T newInstance(IMbeHolder<T> holder);
}
