package conductance.api.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.UnknownNullability;

public interface MaterialRegistry {

	@UnknownNullability
	Block getBlock(Material material, MaterialGenerationHandler handler);

	default ItemStack getBlock(final Material material, final MaterialGenerationHandler handler, final int count) {
		final Block block = this.getBlock(material, handler);
		return block != null ? new ItemStack(block, count) : ItemStack.EMPTY;
	}

	@UnknownNullability
	Item getItem(Material material, MaterialGenerationHandler handler);

	default ItemStack getItem(final Material material, final MaterialGenerationHandler handler, final int count) {
		final Item item = this.getItem(material, handler);
		return item != null ? new ItemStack(item, count) : ItemStack.EMPTY;
	}

	@UnknownNullability
	Fluid getFluid(Material material, MaterialGenerationHandler handler);

	default FluidStack getFluid(final Material material, final MaterialGenerationHandler handler, final int amount) {
		final Fluid fluid = this.getFluid(material, handler);
		return fluid != null ? new FluidStack(fluid, amount) : FluidStack.EMPTY;
	}
}
