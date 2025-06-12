package conductance.core;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import conductance.client.MachineUIFactory;
import conductance.core.cover.CoverCore;
import conductance.core.machine.MachineCore;
import conductance.core.material.MaterialCore;
import conductance.core.periodicelement.PeriodicElementCore;
import conductance.core.register.RegisterCore;
import conductance.core.sync.SyncCore;
import conductance.core.tier.TierCore;
import conductance.init.ConductanceBlockEntities;
import conductance.init.ConductanceBlocks;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceDecoration;
import conductance.init.ConductanceFluids;
import conductance.init.ConductanceItems;
import conductance.loader.PluginEventBus;
import conductance.loader.PluginEventDispatcher;

public final class CommonProxy {

	public static void init(final IEventBus modEventBus) {
		RegisterCore.initialize(modEventBus);
		PluginEventBus.initialize();

		NeoForge.EVENT_BUS.addListener(CommonProxy::handleRightClickBlock);
		UIFactory.register(MachineUIFactory.INSTANCE);

		ConductanceCreativeTabs.init();

		SyncCore.initialize(modEventBus);
		TierCore.initialize(modEventBus);
		PeriodicElementCore.initialize();
		MaterialCore.initialize(modEventBus);
		PluginEventDispatcher.dispatchRegisterRecipeElementTypes();
		PluginEventDispatcher.dispatchRegisterRecipeTypes();
		CoverCore.initialize();

		ConductanceItems.init();
		ConductanceBlocks.init();
		ConductanceFluids.init();
		ConductanceBlockEntities.init();
		MachineCore.initialize(modEventBus);
		ConductanceDecoration.init();
	}

	private static void handleRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
		final UseOnContext ctx = new UseOnContext(event.getLevel(), event.getEntity(), event.getHand(), event.getItemStack(), event.getHitVec());
		final Direction side = InteractionHelper.getInteractSide(event.getHitVec());
		if (ExtendedInteractionHelper.shouldUseExtendedInteraction(ctx)) {
			final InteractionResult result = ExtendedInteractionHelper.handleExtendedInteraction(ctx, side);
			if (result.consumesAction()) {
				event.setCanceled(true);
				event.setCancellationResult(result);
			}
		}
	}

	private CommonProxy() {
	}
}
