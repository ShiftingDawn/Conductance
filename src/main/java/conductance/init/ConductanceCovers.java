package conductance.init;

import java.util.Collections;
import java.util.HashMap;
import net.minecraft.Util;
import conductance.api.CAPI;
import conductance.api.NCCovers;
import conductance.api.plugin.CoverRegister;
import conductance.cover.ConveyorCoverEntity;

public final class ConductanceCovers {

	public static void init(final CoverRegister register) {
		NCCovers.CONVEYORS = Collections.unmodifiableMap(Util.make(new HashMap<>(), map -> CAPI.tiers().getTiers().forEach(tier ->
				map.put(tier, register.register("%s_conveyor".formatted(tier.getRegistryKey()), "conveyor/base", ConveyorCoverEntity::new))
		)));
	}

	private ConductanceCovers() {
	}
}
