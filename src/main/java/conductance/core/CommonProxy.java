package conductance.core;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import conductance.api.util.IInteractable;
import conductance.client.MachineUIFactory;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.PluginManager;
import conductance.core.apiimpl.TierImpl;
import conductance.core.sync.SyncFieldSerializerRegisterImpl;
import conductance.init.ConductanceBlockEntities;
import conductance.init.ConductanceBlocks;
import conductance.init.ConductanceCreativeTabs;
import conductance.init.ConductanceDecoration;
import conductance.init.ConductanceFluids;
import conductance.init.ConductanceItems;
import conductance.loader.PluginEventDispatcher;

public final class CommonProxy {

	public static void init(final IEventBus modEventBus) {
		ApiBridge.init(modEventBus);
		NeoForge.EVENT_BUS.addListener(CommonProxy::handleRightClickBlock);
		NeoForge.EVENT_BUS.addListener(CommonProxy::handleLeftClickBlock);

		UIFactory.register(MachineUIFactory.INSTANCE);

		PluginManager.init();
		ConductanceCreativeTabs.init();

		PluginEventDispatcher.dispatchRegisterSyncFieldSerializers(SyncFieldSerializerRegisterImpl.INSTANCE);
		PluginEventDispatcher.dispatchRegisterTiers(TierImpl.Builder::new);
		PluginEventDispatcher.dispatchRegisterMaterialOreTypes();
		PluginEventDispatcher.dispatchRegisterPeriodicElements();
		PluginEventDispatcher.dispatchRegisterMaterialTextureTypes();
		PluginEventDispatcher.dispatchRegisterMaterialTextureSets();
		PluginEventDispatcher.dispatchRegisterMaterialTraits();
		PluginEventDispatcher.dispatchRegisterMaterialFlags();
		PluginEventDispatcher.dispatchRegisterMaterialTaggedSets();
		PluginEventDispatcher.dispatchRegisterMaterials();
		PluginEventDispatcher.dispatchRegisterMaterialOverrides();
		PluginEventDispatcher.dispatchRegisterMaterialUnitOverrides();
		PluginEventDispatcher.dispatchRegisterRecipeElementTypes();
		PluginEventDispatcher.dispatchRegisterRecipeTypes();
		PluginEventDispatcher.dispatchRegisterCovers();

		ConductanceItems.init();
		ConductanceBlocks.init();
		ConductanceFluids.init();
		ConductanceBlockEntities.init();
		PluginManager.dispatchRegisterMachines();
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
				return;
			}
		}
		final BlockState blockState = event.getLevel().getBlockState(event.getPos());
		if (blockState.getBlock() instanceof final IInteractable interactable) {
			final InteractionResult result = interactable.onRightClick(blockState, event.getLevel(), event.getPos(), event.getEntity(), event.getHand(), event.getHitVec());
			if (result.consumesAction()) {
				event.setCanceled(true);
				event.setCancellationResult(result);
			}
		}
	}

	private static void handleLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
		final BlockState blockState = event.getLevel().getBlockState(event.getPos());
		if (blockState.hasBlockEntity()) {
			final BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
			if (blockEntity instanceof final IInteractable interactable) {
				if (interactable.onLeftClick(event.getEntity(), event.getLevel(), event.getHand(), event.getPos(), event.getFace())) {
					event.setCanceled(true);
					return;
				}
			}
		}
		if (blockState.getBlock() instanceof final IInteractable interactable) {
			if (interactable.onLeftClick(event.getEntity(), event.getLevel(), event.getHand(), event.getPos(), event.getFace())) {
				event.setCanceled(true);
			}
		}
	}

	private CommonProxy() {
	}
}
