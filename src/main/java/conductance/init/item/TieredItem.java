package conductance.init.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import lombok.Getter;
import conductance.api.CAPI;
import conductance.api.util.TieredItemType;
import conductance.api.util.tier.Tier;
import conductance.runtimepack.client.TierItemModelHandler;

public class TieredItem extends ConductanceItem {

	@Getter
	private final TieredItemType type;
	@Getter
	private final Tier tier;

	public TieredItem(final Properties properties, final TieredItemType type, final Tier tier) {
		super(properties);
		this.type = type;
		this.tier = tier;
		if (CAPI.isClient()) {
			TierItemModelHandler.add(this, type);
		}
	}

	@Override
	public Component getDescription() {
		return CAPI.translations().makeLocalizedName(this, () ->
				Component.literal(this.type.getLocalizedNameFactory().formatted(this.tier.getLocalizedNameUnformatted()))
		);
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemColor handleColorTint() {
		return (stack, tintIndex) -> {
			if (tintIndex == 0 && stack.getItem() instanceof final TieredItem tieredItem) {
				return tieredItem.tier.getColor();
			}
			return -1;
		};
	}
}
