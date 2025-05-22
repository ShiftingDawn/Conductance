package conductance.init.cover;

import net.minecraft.core.Direction;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverManager;
import conductance.api.capability.cover.CoverType;

public final class ConveyorCoverEntity extends CoverEntity<ConveyorCoverEntity> {

	public ConveyorCoverEntity(final CoverManager manager, final CoverType<ConveyorCoverEntity> coverType, final Direction side) {
		super(manager, coverType, side);
	}
}
