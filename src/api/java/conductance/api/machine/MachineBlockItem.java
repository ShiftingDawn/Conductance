package conductance.api.machine;

import java.util.function.Consumer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import lombok.Getter;

public class MachineBlockItem<T extends MachineBlockEntity<T>> extends BlockItem {

	private final @Getter MachineType<T> machineType;

	public MachineBlockItem(final MachineBlock<T> machineBlock, final Properties properties) {
		super(machineBlock, properties.component(DataComponents.ITEM_NAME, machineBlock.getMachineType().getName()));
		this.machineType = machineBlock.getMachineType();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final TooltipDisplay tooltipDisplay, final Consumer<Component> tooltipAdder, final TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
		final String machineDescription = "%s.description".formatted(this.getMachineType().getDescriptionId());
		if (I18n.exists(machineDescription)) {
			tooltipAdder.accept(Component.translatable(machineDescription));
		}
		if (this.machineType.getTooltipFactory() != null) {
			this.machineType.getTooltipFactory().get().forEach(tooltipAdder);
		}
	}
}
