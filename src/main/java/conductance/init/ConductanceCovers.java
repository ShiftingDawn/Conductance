package conductance.init;

import conductance.api.NCCovers;
import conductance.api.plugin.CoverRegister;
import conductance.cover.ConveyorCoverEntity;

public final class ConductanceCovers {

	public static void init(final CoverRegister register) {
		NCCovers.LV_CONVEYOR = register.register("lv_conveyor", "conveyor/base", ConveyorCoverEntity::new);
	}

	private ConductanceCovers() {
	}
}
