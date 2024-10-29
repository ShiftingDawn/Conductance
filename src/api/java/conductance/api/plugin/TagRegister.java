package conductance.api.plugin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.util.Marker;

public interface TagRegister {

	void item(TagKey<Item> tag, ItemLike value, ItemLike... moreValues);

	<MARKER extends Material & Marker> void item(TaggedMaterialSet tag, MARKER marker, ItemLike value, ItemLike... moreValues);

	<MARKER extends Material & Marker> void item(TaggedMaterialSet tag, MARKER marker, Material value, Material... moreValues);
}
