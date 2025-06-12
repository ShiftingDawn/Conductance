package conductance.core;

import net.neoforged.bus.api.IEventBus;
import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import conductance.client.MachineUIFactory;
import conductance.core.cover.CoverCore;
import conductance.core.machine.MachineCore;
import conductance.core.material.MaterialCore;
import conductance.core.periodicelement.PeriodicElementCore;
import conductance.core.recipe.RecipeCore;
import conductance.core.register.RegisterCore;
import conductance.core.sync.SyncCore;
import conductance.core.tier.TierCore;
import conductance.init.ConductanceBlockEntities;
import conductance.init.ConductanceBlocks;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceDecoration;
import conductance.init.ConductanceFluids;
import conductance.init.ConductanceItems;

public final class CommonProxy {

	public static void init(final IEventBus modEventBus) {
		RegisterCore.initialize(modEventBus);

		UIFactory.register(MachineUIFactory.INSTANCE);

		ConductanceCreativeTabs.init();

		SyncCore.initialize(modEventBus);
		TierCore.initialize(modEventBus);
		PeriodicElementCore.initialize();
		MaterialCore.initialize(modEventBus);
		RecipeCore.initialize(modEventBus);
		CoverCore.initialize();

		ConductanceItems.init();
		ConductanceBlocks.init();
		ConductanceFluids.init();
		ConductanceBlockEntities.init();
		MachineCore.initialize(modEventBus);
		ConductanceDecoration.init();
	}

	private CommonProxy() {
	}
}
