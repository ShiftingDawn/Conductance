package conductance.init.item;

import net.minecraft.world.item.Item;
import conductance.api.NCDataComponents;

public final class ProgramCircuitItem extends Item {

	public ProgramCircuitItem(final Properties properties) {
		super(properties.component(NCDataComponents.PROGRAM_CIRCUIT, 0));
	}
}
