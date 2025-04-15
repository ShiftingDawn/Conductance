package conductance.api.util;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.util.tier.Tier;

public final class MiscUtils {

	@Nullable
	public static TagKey<Item> getItemTag(final TaggedMaterialSet tagType, final Material material) {
		return tagType.streamItemTags(material).findFirst().orElse(null);
	}

	@Nullable
	public static TagKey<Fluid> getFluidTag(final TaggedMaterialSet tagType, final Material material) {
		return tagType.streamFluidTags(material).findFirst().orElse(null);
	}

	public static Direction getNeighborSide(final BlockPos selfPos, final BlockPos neighborPos) {
		final BlockPos delta = neighborPos.subtract(selfPos);
		return Optional.ofNullable(Direction.fromDelta(delta.getX(), delta.getY(), delta.getX())).orElse(Direction.NORTH);
	}

	public static void explode(final MachineBlockEntity<?> machine, final long voltage) {
		MiscUtils.explode(machine.getLevel(), machine.getBlockPos(), CAPI.tiers().getTierByVoltage(voltage));
	}

	public static void explode(final MachineBlockEntity<?> machine, final Tier energyTier) {
		MiscUtils.explode(machine.getLevel(), machine.getBlockPos(), energyTier);
	}

	public static void explode(final Level level, final BlockPos pos, final Tier energyTier) {
		MiscUtils.explode(level, pos, energyTier.getIndex() + 1);
	}

	public static void explode(final Level level, final BlockPos pos, final float explosionPower) {
		level.removeBlock(pos, false);
		level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosionPower, Level.ExplosionInteraction.BLOCK);
	}

	private MiscUtils() {
	}
}
