package conductance.init.block;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import conductance.api.coil.CoilBlockType;
import conductance.api.util.TextHelper;

public class CoilBlockItem extends BlockItem {

	public CoilBlockItem(final CoilBlock block, final Properties properties) {
		super(block, properties.component(DataComponents.ITEM_NAME, block.getName()));
	}

	@Override
	public CoilBlock getBlock() {
		return (CoilBlock) super.getBlock();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final TooltipDisplay tooltipDisplay, final Consumer<Component> tooltipAdder, final TooltipFlag flag) {
		final CoilBlockType type = this.getBlock().getType();
		tooltipAdder.accept(Component.translatable("tooltip.coil_block.level", type.getIndex() + 1));
		tooltipAdder.accept(Component.translatable("tooltip.coil_block.temperature", TextHelper.NUMBER_FORMAT.format(type.getTemperature())));
	}
}
