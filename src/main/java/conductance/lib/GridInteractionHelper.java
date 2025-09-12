package conductance.lib;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import conductance.api.NCCapabilities;
import conductance.api.cover.CoverEntity;
import conductance.api.cover.CoverManager;
import conductance.api.cover.CoverType;
import conductance.api.cover.ICoverItem;
import conductance.api.cover.ICoverable;
import conductance.api.util.world.IExtendedInteractable;
import conductance.api.util.world.InteractType;
import conductance.api.util.world.InteractionHelper;
import conductance.api.util.world.RotationState;
import conductance.Conductance;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

@EventBusSubscriber(modid = Conductance.MODID)
final class GridInteractionHelper {

	@SubscribeEvent
	private static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
		final UseOnContext ctx = new UseOnContext(event.getLevel(), event.getEntity(), event.getHand(), event.getItemStack(), event.getHitVec());
		final Direction side = InteractionHelper.getLogicalSideFromGrid(event.getHitVec());
		if (InteractionHelper.shouldInteractUsingGrid(ctx)) {
			final InteractionResult result = GridInteractionHelper.handleExtendedInteraction(ctx, side);
			if (result.consumesAction()) {
				event.setCanceled(true);
				event.setCancellationResult(result);
			}
		}
	}

	private static InteractionResult handleExtendedInteraction(final UseOnContext ctx, final Direction side) {
		final InteractType interactType = InteractType.findTypeForStack(ctx.getItemInHand());
		final BlockEntity blockEntity = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (interactType != null) {
			final BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
			if (blockState.getBlock() instanceof final IExtendedInteractable interactable) {
				final InteractionResult res = interactable.onToolUsed(interactType, ctx, side);
				if (res.consumesAction()) {
					return res;
				}
			}
			if (blockEntity instanceof final IExtendedInteractable interactable) {
				final InteractionResult res = interactable.onToolUsed(interactType, ctx, side);
				if (res.consumesAction()) {
					return res;
				}
			}
		}
		final ICoverable coverable = NCCapabilities.getCoverable(ctx.getLevel(), ctx.getClickedPos());
		if (coverable != null && ctx.getPlayer() != null) {
			final CoverManager coverManager = coverable.getCoverManager();
			final Optional<? extends CoverEntity<?>> cover = coverManager.getCover(side);
			if (cover.isPresent()) {
				final CoverEntity<?> coverEntity = cover.get();
				if (interactType == InteractType.HAMMER) {
					final ItemStack coverItem = coverEntity.getAttachItem();
					final CoverType<?> removedCover = coverManager.removeCover(side);
					if (removedCover != null) {
						if (!ctx.getLevel().isClientSide) {
							if (!ctx.getPlayer().isCreative() && !ctx.getPlayer().addItem(coverItem)) {
								Block.popResourceFromFace(ctx.getLevel(), ctx.getClickedPos(), ctx.getClickedFace(), coverItem);
							}
						}
						//TODO play cover break sound
						//						ConductanceSounds.TOOL_CROWBAR.play(ctx.getLevel(), ctx.getPlayer(), ctx.getClickedPos(), 1.0f, 0.15f);
					}
					return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
				} else if (ctx.getPlayer().isCrouching() && ctx.getPlayer().getItemInHand(ctx.getHand()).isEmpty()) {
					//TODO handle gui open
					//					if (!ctx.getLevel().isClientSide) {
					//						CoverGuiFactory.INSTANCE.openUI(coverEntity, (ServerPlayer) ctx.getPlayer());
					//					}
					return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
				}
			} else if (ctx.getItemInHand().getItem() instanceof final ICoverItem<?> coverItem) {
				if (!ctx.getLevel().isClientSide && coverManager.canAcceptCover(coverItem.getCoverType(), side)) {
					if (coverManager.attachCover(coverItem.getCoverType(), side, ctx.getItemInHand(), (ServerPlayer) ctx.getPlayer()) && !ctx.getPlayer().isCreative()) {
						ctx.getItemInHand().shrink(1);
					}
				}
				return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
			}
		}
		if (interactType != null) {
			if (interactType == InteractType.WRENCH && GridInteractionHelper.tryWrench(ctx, side)) {
				//TODO play wrench sound
				//				if (ctx.getLevel().isClientSide) {
				//					ConductanceSounds.TOOL_WRENCH.play(ctx.getLevel(), ctx.getPlayer());
				//				}
				return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
			}
		}
		return InteractionResult.PASS;
	}

	private static boolean tryWrench(final UseOnContext ctx, final Direction side) {
		final Player player = ctx.getPlayer();
		final BlockPos pos = ctx.getClickedPos();
		final Level level = ctx.getLevel();
		final BlockState state = level.getBlockState(pos);
		if (player != null && player.isCrouching()) {
			if (state.hasProperty(HORIZONTAL_FACING) && side != state.getValue(HORIZONTAL_FACING) && side.get2DDataValue() != -1) {
				level.setBlockAndUpdate(pos, state.setValue(HORIZONTAL_FACING, side));
				return true;
			} else if (state.hasProperty(FACING) && side != state.getValue(FACING)) {
				level.setBlockAndUpdate(pos, state.setValue(FACING, side));
				return true;
			} else if (state.hasProperty(RotationState.ALL.property) && side != state.getValue(RotationState.ALL.property)) {
				level.setBlockAndUpdate(pos, state.setValue(RotationState.ALL.property, side));
				return true;
			}
		}
		return false;
	}

	private GridInteractionHelper() {
	}
}
