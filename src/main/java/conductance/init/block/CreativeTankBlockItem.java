package conductance.init.block;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

public final class CreativeTankBlockItem extends BlockItem {

	public CreativeTankBlockItem(final Block block, final Properties properties) {
		super(block, properties.useBlockDescriptionPrefix());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final TooltipDisplay tooltipDisplay, final Consumer<Component> tooltipAdder, final TooltipFlag flag) {
		tooltipAdder.accept(Component.translatable("tooltip.creative_tank.description.1"));
		tooltipAdder.accept(Component.translatable("tooltip.creative_tank.description.2"));
	}
}
