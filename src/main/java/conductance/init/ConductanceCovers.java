package conductance.init;

import java.util.Collections;
import java.util.HashMap;
import net.minecraft.Util;
import conductance.api.CAPI;
import conductance.api.NCCovers;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterCoverEvent;
import conductance.Conductance;
import conductance.init.cover.ConveyorCoverEntity;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceCovers {

	@EventListener
	private static void init(final RegisterCoverEvent event) {
		NCCovers.CONVEYORS = Collections.unmodifiableMap(Util.make(new HashMap<>(), map -> CAPI.tiers().getTiers().forEach(tier ->
				map.put(tier, event.register("%s_conveyor".formatted(tier.getRegistryKey()), "conveyor/base", ConveyorCoverEntity::new))
		)));
	}

	private ConductanceCovers() {
	}
}
