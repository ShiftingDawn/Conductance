package conductance.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import conductance.api.CAPI;

public class ConductanceItem extends Item implements IConductanceItem {

	public ConductanceItem(Properties properties) {
		super(properties);
	}

	@Override
	public Component getDescription() {
		return CAPI.TRANSLATIONS.makeLocalizedName(this);
	}

	@Override
	public Component getName(final ItemStack stack) {
		return this.getDescription();
	}
}
