package conductance.api.util;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

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

	private MiscUtils() {
	}
}
