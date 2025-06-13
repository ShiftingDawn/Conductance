package conductance.init.block;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import conductance.core.block.IMaterialOreBlock;

public class MaterialOreBlockItem extends ConductanceBlockItem {

	public MaterialOreBlockItem(final Block block, final Properties properties) {
		super(block, properties);
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
