package conductance.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.tier.Tier;

public final class BlockHelper {

	public static void explode(final MachineBlockEntity<?> machine, final long voltage) {
		if (machine.getLevel() == null) {
			return;
		}
		BlockHelper.explode(machine.getLevel(), machine.getBlockPos(), CAPI.tiers().getByVoltage(voltage));
	}

	public static void explode(final MachineBlockEntity<?> machine, final Tier energyTier) {
		if (machine.getLevel() == null) {
			return;
		}
		BlockHelper.explode(machine.getLevel(), machine.getBlockPos(), energyTier);
	}

	public static void explode(final Level level, final BlockPos pos, final Tier energyTier) {
		BlockHelper.explode(level, pos, energyTier.getIndex() + 1);
	}

	public static void explode(final Level level, final BlockPos pos, final float explosionPower) {
		level.removeBlock(pos, false);
		level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosionPower, Level.ExplosionInteraction.BLOCK);
	}

	private BlockHelper() {
	}
}
