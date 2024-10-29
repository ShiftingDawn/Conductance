package conductance.block;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import conductance.item.IConductanceItem;

public class MaterialOreBlockItem extends BlockItem implements IConductanceItem {

	public MaterialOreBlockItem(final Block block, final Properties properties) {
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

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if (stack.getItem() instanceof final MaterialOreBlockItem blockItem) {
				return ((IMaterialOreBlock) blockItem.getBlock()).getMaterial().getTintColor(tintIndex);
			}
			return -1;
		};
	}
}
