package conductance.core;

import net.neoforged.bus.api.IEventBus;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.PluginManager;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceItems;

public class CommonProxy {

	public void init(final IEventBus modEventBus) {
		ApiBridge.init(modEventBus);
		ConductanceCreativeTabs.init();

		PluginManager.init();

		PluginManager.dispatchPeriodicElements();

		PluginManager.dispatchMaterialTextureTypes();
		PluginManager.dispatchMaterialTextureSets();
		PluginManager.dispatchMaterialTraits();
		PluginManager.dispatchMaterialFlags();
		PluginManager.dispatchMaterialTaggedSets();
		PluginManager.dispatchMaterials();

		PluginManager.dispatchMaterialOverrides();
		PluginManager.dispatchMaterialUnitOverrides();
		ConductanceItems.init();
	}
}
