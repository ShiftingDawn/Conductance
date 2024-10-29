package conductance.api.util;

import javax.annotation.Nullable;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public final class MiscUtils {

	@Nullable
	public static TagKey<Item> getItemTag(final TaggedMaterialSet tagType, final Material material) {
		return tagType.streamItemTags(material).findFirst().orElse(null);
	}

	private MiscUtils() {
	}
}
