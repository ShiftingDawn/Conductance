package conductance.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface IInteractable {

	default InteractionResult onRightClick(final BlockState blockState, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hit) {
		return InteractionResult.PASS;
	}

	default boolean onLeftClick(final Player player, final Level level, final InteractionHand hand, final BlockPos pos, @Nullable final Direction side) {
		return false;
	}
}
