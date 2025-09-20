package conductance.init.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import lombok.Getter;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredItemType;

public final class TieredItem extends Item {

	private final @Getter TieredItemType type;
	private final @Getter Tier tier;

	public TieredItem(final Properties properties, final TieredItemType type, final Tier tier) {
		super(properties.component(DataComponents.ITEM_NAME, Component.translatable(type.getDescriptionId(), tier.getName())));
		this.type = type;
		this.tier = tier;
	}
}
