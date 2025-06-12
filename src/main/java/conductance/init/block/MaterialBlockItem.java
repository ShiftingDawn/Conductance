package conductance.init.block;

import javax.annotation.Nonnull;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.init.item.IConductanceItem;

public class MaterialBlockItem extends ConductanceBlockItem implements IConductanceItem {

	public MaterialBlockItem(final MaterialBlock block, final Properties properties) {
		super(block, properties);
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
		final int time = this.getBlock().getMaterial().getBurnTime();
		return Math.max(0, (int) (time * this.getBlock().getSet().getUnitValue(this.getBlock().getMaterial()) / CAPI.UNIT));
	}
}
