package conductance.core;

import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.PluginManager;
import net.neoforged.bus.api.IEventBus;

public class CommonProxy {

	public void init(final IEventBus modEventBus) {
		ApiBridge.init();

		PluginManager.init();
	}
}