package conductance.api.registry;

import java.util.Optional;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public interface TaggedSetRegistry<TYPE, SET extends TaggedSet<TYPE>> {

	Optional<Item> getItem(SET taggedSet, TYPE object);

	ItemStack getItem(SET taggedSet, TYPE object, int count);

	@Nullable
	Item getItemUnsafe(SET taggedSet, TYPE object);

	Optional<Block> getBlock(SET taggedSet, TYPE object);

	ItemStack getBlock(SET taggedSet, TYPE object, int count);

	@Nullable
	Block getBlockUnsafe(SET taggedSet, TYPE object);

	Optional<Fluid> getFluid(SET taggedSet, TYPE object);

	FluidStack getFluid(SET taggedSet, TYPE object, int amount);

	@Nullable
	Fluid getFluidUnsafe(SET taggedSet, TYPE object);

	Optional<BucketItem> getBucket(SET taggedSet, TYPE object);

	@Nullable
	BucketItem getBucketUnsafe(SET taggedSet, TYPE object);
}
