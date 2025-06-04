package conductance.api.util.world;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.tier.Tier;

public final class WorldUtils {

	public static Direction getNeighborSide(final BlockPos selfPos, final BlockPos neighborPos) {
		final BlockPos delta = neighborPos.subtract(selfPos);
		return Optional.ofNullable(Direction.fromDelta(delta.getX(), delta.getY(), delta.getZ())).orElse(Direction.NORTH);
	}

	public static void explode(final MachineBlockEntity<?> machine, final long voltage) {
		WorldUtils.explode(machine.getLevel(), machine.getBlockPos(), CAPI.tiers().getTierByVoltage(voltage));
	}

	public static void explode(final MachineBlockEntity<?> machine, final Tier energyTier) {
		WorldUtils.explode(machine.getLevel(), machine.getBlockPos(), energyTier);
	}

	public static void explode(final Level level, final BlockPos pos, final Tier energyTier) {
		WorldUtils.explode(level, pos, energyTier.getIndex() + 1);
	}

	public static void explode(final Level level, final BlockPos pos, final float explosionPower) {
		level.removeBlock(pos, false);
		level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosionPower, Level.ExplosionInteraction.BLOCK);
	}

	public static void requestRenderUpdate(@Nullable final Level level, final BlockPos pos, @Nullable final BlockState state) {
		if (level == null) {
			return;
		}
		final BlockState state2 = Objects.requireNonNullElseGet(state, () -> level.getBlockState(pos));
		if (level.isClientSide) {
			level.sendBlockUpdated(pos, state2, state2, 1 << 3);
		} else {
			level.blockEvent(pos, state2.getBlock(), 1, 0);
		}
	}

	private WorldUtils() {
	}
}
