package conductance.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.tier.Tier;

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
		return Optional.ofNullable(Direction.fromDelta(delta.getX(), delta.getY(), delta.getZ())).orElse(Direction.NORTH);
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

	@SuppressWarnings("deprecation")
	public static Map<Item, Integer> getFurnaceFuels() {
		return Util.make(new HashMap<>(), map -> {
			map.putAll(FurnaceBlockEntity.getFuel());
			BuiltInRegistries.ITEM.forEach(item -> {
				final int fuelTime = item.getDefaultInstance().getBurnTime(RecipeType.SMELTING);
				if (fuelTime > 0) {
					map.put(item, fuelTime);
				}
			});
		});
	}

	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T readEnumFromNbt(final CompoundTag nbt, final String key, final T fallback) {
		if (nbt.contains(key, Tag.TAG_INT)) {
			final int ordinal = nbt.getInt(key);
			if (ordinal >= 0 && ordinal < fallback.getClass().getEnumConstants().length) {
				return (T) fallback.getClass().getEnumConstants()[ordinal];
			}
		}
		return fallback;
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

	private MiscUtils() {
	}
}
