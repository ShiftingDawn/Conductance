package conductance.block;

import javax.annotation.Nonnull;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.item.IConductanceItem;

public class MaterialBlockItem extends BlockItem implements IConductanceItem {

	public MaterialBlockItem(final Block block, final Properties properties) {
		super(block, properties);
	}

	@Override
	public String getDescriptionId() {
		return this.getBlock().getDescriptionId();
	}

	@Override
	public String getDescriptionId(final ItemStack stack) {
		return this.getDescriptionId();
	}

	@Override
	public Component getDescription() {
		return this.getBlock().getName();
	}

	@Override
	public Component getName(final ItemStack stack) {
		return this.getDescription();
	}

	@Override
	@Nonnull
	public MaterialBlock getBlock() {
		return (MaterialBlock) super.getBlock();
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if (stack.getItem() instanceof final MaterialBlockItem blockItem) {
				return blockItem.getBlock().getMaterial().getTintColor(tintIndex);
			}
			return -1;
		};
	}

	@Override
	public int getBurnTime(final ItemStack itemStack, @Nullable final RecipeType<?> recipeType) {
		final int time = this.getBlock().getMaterial().getData().getBurnTime();
		return time <= 0 ? -1 : (int) (time * this.getBlock().getSet().getUnitValue(this.getBlock().getMaterial()) / CAPI.UNIT);
	}
}