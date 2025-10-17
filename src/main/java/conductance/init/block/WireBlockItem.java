package conductance.init.block;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import conductance.api.util.TextHelper;
import conductance.lib.pipenet.WireData;

public final class WireBlockItem extends PipeBlockItem {

	public WireBlockItem(final WireBlock block, final Properties properties) {
		super(block, properties.component(DataComponents.ITEM_NAME, block.getName()));
	}

	@Override
	public WireBlock getBlock() {
		return (WireBlock) super.getBlock();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final TooltipDisplay tooltipDisplay, final Consumer<Component> tooltipAdder, final TooltipFlag flag) {
		if (stack.getItem() instanceof final WireBlockItem wireBlockItem) {
			final WireData data = wireBlockItem.getBlock().getRealProps();
			tooltipAdder.accept(Component.translatable("tooltip.wire.transfer",
				TextHelper.NUMBER_FORMAT.format(data.amperage()),
				TextHelper.NUMBER_FORMAT.format(data.voltage()),
				TextHelper.ENERGY_FORMAT, data.getTier().getName())
			);
		}
	}
}
