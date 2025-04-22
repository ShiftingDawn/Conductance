package conductance.api.machine;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class MachineBlockItem<T extends MachineBlockEntity<T>> extends BlockItem implements IMachineBlockItem<T> {

	public MachineBlockItem(final IMachineBlock<T> block, final Properties properties) {
		super((Block) Util.make(block, block2 -> {
			if (!(block2 instanceof Block)) {
				throw new IllegalArgumentException("Machine block %s is not a Block".formatted(block2.getClass().getName()));
			}
		}), properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MachineType<T> getMachineType() {
		return ((IMachineBlock<T>) this.getBlock()).getMachineType();
	}

	@Override
	public Component getName(final ItemStack stack) {
		final String localized = this.getMachineType().getLocalizedName();
		return localized != null ? Component.literal(localized) : super.getName(stack);
	}

	@Override
	public Component getDescription() {
		final String localized = this.getMachineType().getLocalizedName();
		return localized != null ? Component.literal(localized) : super.getDescription();
	}
}
