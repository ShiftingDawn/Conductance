package conductance.init.item;

import lombok.Getter;
import conductance.api.cover.CoverEntity;
import conductance.api.cover.CoverType;
import conductance.api.cover.ICoverItem;
import conductance.api.tier.TieredItemType;
import conductance.api.tier.Tier;

public final class TieredCoverItem<COVER extends CoverEntity<COVER>> extends TieredItem implements ICoverItem<COVER> {

	@Getter
	private final CoverType<COVER> coverType;

	public TieredCoverItem(final Properties properties, final TieredItemType type, final Tier tier, final CoverType<COVER> coverType) {
		super(properties, type, tier);
		this.coverType = coverType;
	}
}
