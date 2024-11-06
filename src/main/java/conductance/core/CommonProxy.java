package conductance.core;

import net.neoforged.bus.api.IEventBus;
import nl.appelgebakje22.xdata.XData;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.PluginManager;
import conductance.init.ConductanceBlocks;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceFluids;
import conductance.init.ConductanceItems;

public final class CommonProxy {

	public static void init(final IEventBus modEventBus) {
		ApiBridge.init(modEventBus);

		PluginManager.init();
		XData.init(PluginManager::dispatchRegisterXDataExtensions);

		ConductanceCreativeTabs.init();

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

		PluginManager.dispatchRecipeElementTypes();
		PluginManager.dispatchRecipeTypes();

		ConductanceItems.init();
		ConductanceBlocks.init();
		ConductanceFluids.init();

		PluginManager.dispatchRegisterMachines();
	}

	private CommonProxy() {
	}
}
