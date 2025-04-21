package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IAppearance {

	default BlockState getAppearance(final BlockState state, final BlockAndTintGetter level, final BlockPos pos, final Direction side, @Nullable final BlockState queryState, @Nullable final BlockPos queryPos) {
		return state;
	}
}
