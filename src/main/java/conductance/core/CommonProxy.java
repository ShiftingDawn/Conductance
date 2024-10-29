package conductance.core;

import net.neoforged.bus.api.IEventBus;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.PluginManager;
import conductance.init.ConductanceBlocks;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceFluids;
import conductance.init.ConductanceItems;

public final class CommonProxy {

	public static void init(final IEventBus modEventBus) {
		ApiBridge.init(modEventBus);
		ConductanceCreativeTabs.init();

		PluginManager.init();

		PluginManager.dispatchPeriodicElements();

		PluginManager.dispatchMaterialTextureTypes();
		PluginManager.dispatchMaterialTextureSets();
		PluginManager.dispatchMaterialTraits();
		PluginManager.dispatchMaterialFlags();
		PluginManager.dispatchMaterialOreTypes();
		PluginManager.dispatchMaterialTaggedSets();
		PluginManager.dispatchMaterials();

		PluginManager.dispatchMaterialOverrides();
		PluginManager.dispatchMaterialUnitOverrides();

		ConductanceItems.init();
		ConductanceBlocks.init();
		ConductanceFluids.init();

		PluginManager.dispatchRegisterMachines();
	}

	private CommonProxy() {
	}
}
