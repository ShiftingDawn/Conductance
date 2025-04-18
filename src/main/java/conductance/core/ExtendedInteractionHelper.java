package conductance.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.util.IExtendedInteractable;
import conductance.api.util.InteractType;
import conductance.api.util.RotationState;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public final class ExtendedInteractionHelper {

	public static boolean shouldUseExtendedInteraction(final UseOnContext ctx) {
		final BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
		final BlockEntity blockEntity = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		if (blockEntity != null) {
			//TODO covers
//			final ICoverable coverable = CapabilityHelper.getCoverable(ctx.getLevel(), ctx.getClickedPos(), null);
//			if (coverable != null && (ctx.getItemInHand().getItem() instanceof ICoverItem || ctx.getPlayer().isCrouching() || InteractType.CROWBAR.is(ctx.getItemInHand()))) {
//				return true;
//			}
			if (ctx.getPlayer().isCrouching() && (blockState.getBlock() instanceof IExtendedInteractable || blockEntity instanceof IExtendedInteractable)) {
				return true;
			}
			//TODO pipelike
//			if (ctx.getItemInHand().getItem() instanceof final PipeBlockItem pipeBlockItem && blockEntity instanceof final PipeBlockEntity<?, ?> pipeBlockEntity &&
//					pipeBlockItem.getBlock().pipeType.type().equals(pipeBlockEntity.getPipeType().type())) {
//				return true;
//			}
		}
		//Fall back to rotatable blocks
		if (InteractType.WRENCH.is(ctx.getItemInHand())) {
			final BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
			return state.hasProperty(FACING) || state.hasProperty(HORIZONTAL_FACING) || state.hasProperty(RotationState.ALL.property);
		}
		return false;
	}

	public static InteractionResult handleExtendedInteraction(final UseOnContext ctx, final Direction side) {
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
		//TODO covers
//		final ICoverable coverable = CapabilityHelper.getCoverable(ctx.getLevel(), ctx.getClickedPos(), side);
//		if (coverable != null) {
//			final CoverManager coverManager = coverable.getCoverManager();
//			//TODO handle gui open
//			if (coverManager.getCover(side).isPresent()) {
//				if (interactType == InteractType.CROWBAR) {
//					final CoverType<?> removedCover = coverManager.removeCover(side);
//					if (removedCover != null) {
//						if (!ctx.getLevel().isClientSide) {
//							//TODO drop cover
//						}
//						ConductanceSounds.TOOL_CROWBAR.play(ctx.getLevel(), ctx.getPlayer(), ctx.getClickedPos(), 1.0f, 0.15f);
//					}
//					return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
//				} else if (ctx.getPlayer().isCrouching() && ctx.getPlayer().getItemInHand(ctx.getHand()).isEmpty()) {
//					final CoverEntity<?> coverEntity = coverManager.getCover(side).get();
//					if (!ctx.getLevel().isClientSide) {
//						CoverGuiFactory.INSTANCE.openUI(coverEntity, (ServerPlayer) ctx.getPlayer());
//					}
//					return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
//				}
//			} else if (ctx.getItemInHand().getItem() instanceof final ICoverItem<?> coverItem) {
//				if (!ctx.getLevel().isClientSide && coverManager.canAcceptCover(coverItem.getCoverType(), side)) {
//					coverManager.attachCover(coverItem.getCoverType(), side, ctx.getItemInHand(), (ServerPlayer) ctx.getPlayer());
//				}
//				return InteractionResult.sidedSuccess(ctx.getLevel().isClientSide);
//			}
//		}
		if (interactType != null) {
			if (interactType == InteractType.WRENCH && ExtendedInteractionHelper.tryWrench(ctx, side)) {
//				if (ctx.getLevel().isClientSide) {
				//TODO play sound
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

	private ExtendedInteractionHelper() {
	}
}
