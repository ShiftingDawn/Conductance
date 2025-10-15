package conductance.api.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.tier.Tier;
import conductance.api.util.Internal;

public final class BlockHelper {

	public static void explodeOrReplaceWithFire(final MachineBlockEntity<?> machine, final long voltage) {
		if (machine.getLevel() == null) {
			return;
		}
		if (Internal.IS_EXPLOSION_ENABLED.getAsBoolean()) {
			BlockHelper.explode(machine, voltage);
		} else {
			machine.getLevel().setBlockAndUpdate(machine.getBlockPos(), Blocks.FIRE.defaultBlockState());
		}
	}

	public static void explode(final MachineBlockEntity<?> machine, final long voltage) {
		if (machine.getLevel() == null) {
			return;
		}
		BlockHelper.explode(machine.getLevel(), machine.getBlockPos(), CAPI.tiers().getByVoltage(voltage));
	}

	public static void explode(final Level level, final BlockPos pos, final Tier energyTier) {
		BlockHelper.explode(level, pos, energyTier.getIndex() + 1);
	}

	public static void explode(final Level level, final BlockPos pos, final float explosionPower) {
		level.removeBlock(pos, false);
		level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosionPower, Level.ExplosionInteraction.BLOCK);
	}

	public static Direction findNeighborSide(final BlockPos self, final BlockPos neighbor) {
		final BlockPos delta = neighbor.subtract(self);
		return Direction.getNearest(delta.getX(), delta.getY(), delta.getZ(), Direction.NORTH);
	}

	public static void playSound(final UseOnContext ctx, final Supplier<SoundEvent> event) {
		BlockHelper.playSound(ctx, event.get());
	}

	public static void playSound(final UseOnContext ctx, final SoundEvent event) {
		BlockHelper.playSound(ctx.getLevel(), ctx.getPlayer(), event, ctx.getClickedPos());
	}

	public static void playSound(final Level level, @Nullable final Entity player, final SoundEvent event, final BlockPos pos) {
		level.playSound(player, pos, event, SoundSource.BLOCKS);
	}

	private BlockHelper() {
	}
}
