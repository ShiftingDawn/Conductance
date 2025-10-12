package conductance.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import conductance.api.block.FacingAndRotation;
import conductance.api.block.GridInteractionHelper;
import conductance.api.block.IGridInteractable;
import conductance.api.block.InteractType;
import conductance.api.machine.IFluidAutoOutput;
import conductance.api.machine.IItemAutoOutput;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineCapability;
import static conductance.api.NCBlockStateProperties.FACING_ALL;
import static conductance.api.NCBlockStateProperties.FACING_EXTENDED;
import static conductance.api.NCBlockStateProperties.FACING_HORIZONTAL;
import static conductance.api.NCBlockStateProperties.FACING_VERTICAL;

public final class GridInteractionHandler {

	public static void init(final IEventBus eventBus) {
		eventBus.addListener(PlayerInteractEvent.RightClickBlock.class, GridInteractionHandler::onRightClickBlock);
	}

	private static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
		final UseOnContext ctx = new UseOnContext(event.getLevel(), event.getEntity(), event.getHand(), event.getItemStack(), event.getHitVec());
		final Direction side = GridInteractionHelper.getLogicalSideFromGrid(event.getHitVec());
		if (GridInteractionHelper.shouldInteractUsingGrid(ctx)) {
			final InteractionResult result = GridInteractionHandler.handleGridInteraction(ctx, side);
			if (result.consumesAction()) {
				event.setCanceled(true);
				event.setCancellationResult(result);
			}
		}
	}

	private static InteractionResult handleGridInteraction(final UseOnContext ctx, final Direction side) {
		final InteractType interactType = InteractType.findTypeForStack(ctx.getItemInHand());
		final BlockEntity blockEntity = ctx.getLevel().getBlockEntity(ctx.getClickedPos());
		final BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
		if (blockState.getBlock() instanceof final IGridInteractable interactable) {
			final InteractionResult res = interactable.onToolUsed(interactType, ctx, side);
			if (res.consumesAction()) {
				return res;
			}
		}
		if (blockEntity instanceof final IGridInteractable interactable) {
			final InteractionResult res = interactable.onToolUsed(interactType, ctx, side);
			if (res.consumesAction()) {
				return res;
			}
		}
		if (interactType == InteractType.WRENCH && GridInteractionHandler.tryWrench(ctx, side)) {
			// TODO play wrench sound
			// if (ctx.getLevel().isClientSide) {
			// ConductanceSounds.TOOL_WRENCH.play(ctx.getLevel(), ctx.getPlayer());
			//}
			return InteractionResult.SUCCESS_SERVER;
		}
		return InteractionResult.PASS;
	}

	private static boolean tryWrench(final UseOnContext ctx, final Direction side) {
		final Player player = ctx.getPlayer();
		final BlockPos pos = ctx.getClickedPos();
		final Level level = ctx.getLevel();
		final BlockState state = level.getBlockState(pos);
		if (player == null) {
			return false;
		}
		if (player.isCrouching()) {
			if (state.hasProperty(FACING_ALL) && side != state.getValue(FACING_ALL)) {
				level.setBlockAndUpdate(pos, state.setValue(FACING_ALL, side));
				return true;
			}
			if (state.hasProperty(FACING_HORIZONTAL) && side != state.getValue(FACING_HORIZONTAL) && side.get2DDataValue() != -1) {
				level.setBlockAndUpdate(pos, state.setValue(FACING_HORIZONTAL, side));
				return true;
			}
			if (state.hasProperty(FACING_VERTICAL) && side != state.getValue(FACING_VERTICAL) && side.get2DDataValue() == -1) {
				level.setBlockAndUpdate(pos, state.setValue(FACING_VERTICAL, side));
				return true;
			}
			if (state.hasProperty(FACING_EXTENDED)) {
				final FacingAndRotation current = state.getValue(FACING_EXTENDED);
				if (side != current.getFacing()) {
					level.setBlockAndUpdate(pos, state.setValue(FACING_EXTENDED, FacingAndRotation.get(side, current.getRotation())));
					return true;
				}
			}
		} else {
			if (state.hasProperty(FACING_EXTENDED)) {
				final FacingAndRotation current = state.getValue(FACING_EXTENDED);
				if (side == current.getFacing()) {
					final Rotation rotation = Rotation.values()[(current.getRotation().ordinal() + 1) % Rotation.values().length];
					level.setBlockAndUpdate(pos, state.setValue(FACING_EXTENDED, FacingAndRotation.get(side, rotation)));
					return true;
				}
			}
			if (level.getBlockEntity(pos) instanceof final MachineBlockEntity<?> machine) {
				boolean hasChanged = false;
				for (final MachineCapability capability : machine.getCapabilities().values()) {
					if (capability instanceof final IItemAutoOutput itemAutoOutput && side != machine.getFacing()) {
						itemAutoOutput.setItemAutoOutputSide(side);
						hasChanged = true;
					}
					if (capability instanceof final IFluidAutoOutput fluidAutoOutput && side != machine.getFacing()) {
						fluidAutoOutput.setFluidAutoOutputSide(side);
						hasChanged = true;
					}
				}
				if (hasChanged) {
					return true;
				}
			}
		}
		return false;
	}

	private GridInteractionHandler() {
	}
}
