package conductance.init.cover;

import net.minecraft.core.Direction;
import conductance.api.cover.CoverEntity;
import conductance.api.cover.CoverManager;
import conductance.api.cover.CoverType;

public final class ConveyorCoverEntity extends CoverEntity<ConveyorCoverEntity> {

	public ConveyorCoverEntity(final CoverManager manager, final CoverType<ConveyorCoverEntity> coverType, final Direction side) {
		super(manager, coverType, side);
	}
}
