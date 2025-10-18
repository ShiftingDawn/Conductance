package conductance.init.block;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import lombok.Getter;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredItemType;

public final class TieredBlock extends SimpleBlock {

	private final @Getter TieredItemType type;
	private final @Getter Tier tier;
	private final MutableComponent name;

	public TieredBlock(final Properties props, final TieredItemType type, final Tier tier) {
		super(props);
		this.type = type;
		this.tier = tier;
		this.name = Component.translatable(type.getDescriptionId(), tier.getName());
	}

	@Override
	public MutableComponent getName() {
		return this.name;
	}
}
