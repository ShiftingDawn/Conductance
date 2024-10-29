package conductance.core;

import net.neoforged.bus.api.IEventBus;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.PluginManager;

public class CommonProxy {

	public void init(final IEventBus modEventBus) {
		ApiBridge.init();

		PluginManager.init();

		PluginManager.dispatchPeriodicElements();

		PluginManager.dispatchMaterialTextureTypes();
		PluginManager.dispatchMaterialTextureSets();
	}
}
