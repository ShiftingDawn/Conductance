package conductance.init.item;

import lombok.Getter;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverType;
import conductance.api.capability.cover.ICoverItem;

public class SimpleCoverItem<COVER extends CoverEntity<COVER>> extends ConductanceItem implements ICoverItem<COVER> {

	@Getter
	private final CoverType<COVER> coverType;

	public SimpleCoverItem(final Properties properties, final CoverType<COVER> coverType) {
		super(properties);
		this.coverType = coverType;
	}
}
