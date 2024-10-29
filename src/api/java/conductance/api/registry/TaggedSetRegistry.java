package conductance.api.registry;

import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import org.jetbrains.annotations.Nullable;

public interface TaggedSetRegistry<TYPE, SET extends TaggedSet<TYPE>> {

	Optional<Item> getItem(SET SET, TYPE object);

	ItemStack getItem(SET SET, TYPE object, int count);

	@Nullable
	Item getItemUnsafe(SET SET, TYPE object);

	Optional<Block> getBlock(SET SET, TYPE object);

	ItemStack getBlock(SET SET, TYPE object, int count);

	@Nullable
	Block getBlockUnsafe(SET SET, TYPE object);

	// TODO handle fluids
//	Optional<Fluid> getFluid(MaterialFluidGenerator generator, TYPE object);
//
//	FluidStack getFluid(MaterialFluidGenerator generator, TYPE object, long amount);
//
//	@Nullable
//	Fluid getFluidUnsafe(MaterialFluidGenerator generator, TYPE object);
//
//	Optional<BucketItem> getBucket(MaterialFluidGenerator generator, TYPE object);
//
//	@Nullable
//	BucketItem getBucketUnsafe(MaterialFluidGenerator generator, TYPE object);

	void register(SET SET, TYPE object, ItemEntry<? extends Item> item);

	void register(SET SET, TYPE object, BlockEntry<? extends Block> block);

	void register(SET SET, TYPE object, FluidEntry<? extends Fluid> fluid);

}
