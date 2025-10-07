package conductance.init;

import conductance.api.machine.event.RegisterMultiBlockPartCapabilityEvent;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.Conductance;
import static conductance.api.NCMultiBlockPartCapabilities.FLUIDS_IN;
import static conductance.api.NCMultiBlockPartCapabilities.FLUIDS_OUT;
import static conductance.api.NCMultiBlockPartCapabilities.ITEMS_IN;
import static conductance.api.NCMultiBlockPartCapabilities.ITEMS_OUT;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceMultiBlockPartCapabilities {

	@EventListener(priority = -100)
	private static void init(final RegisterMultiBlockPartCapabilityEvent event) {
		ITEMS_IN = event.register("items_in");
		ITEMS_OUT = event.register("items_out");
		FLUIDS_IN = event.register("fluids_in");
		FLUIDS_OUT = event.register("fluids_out");
	}

	private ConductanceMultiBlockPartCapabilities() {
	}
}
