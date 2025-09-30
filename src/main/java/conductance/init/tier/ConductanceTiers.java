package conductance.init.tier;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.tier.event.RegisterTierEvent;
import conductance.Conductance;
import static conductance.api.NCTiers.LV;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceTiers {

	@EventListener
	private static void init(final RegisterTierEvent event) {
		LV = event.register("lv", 0x649BFF, LvComponentMap::new);
	}

	private ConductanceTiers() {
	}
}
