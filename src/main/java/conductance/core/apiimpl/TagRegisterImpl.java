package conductance.core.apiimpl;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.TagRegister;
import conductance.api.util.Marker;
import conductance.api.util.MiscUtils;
import conductance.core.datapack.TagGenerationHandler;

final class TagRegisterImpl implements TagRegister {

	public static final TagRegisterImpl INSTANCE = new TagRegisterImpl();

	@Override
	public void item(final TagKey<Item> tag, final ItemLike value, final ItemLike... moreValues) {
		TagGenerationHandler.register(tag, value);
		for (final ItemLike item : moreValues) {
			TagGenerationHandler.register(tag, item);
		}
	}

	@Override
	public <MARKER extends Material & Marker> void item(TaggedMaterialSet tag, MARKER marker, ItemLike value, ItemLike... moreValues) {
		final TagKey<Item> tagKey = MiscUtils.getItemTag(tag, marker);
		if (tagKey != null) {
			this.item(tagKey, value, moreValues);
		}
	}

	@Override
	public <MARKER extends Material & Marker> void item(TaggedMaterialSet tag, MARKER marker, Material value, Material... moreValues) {
		CAPI.MATERIALS.getItem(tag, value).ifPresent(item -> this.item(tag, marker, item));
		for (final Material material : moreValues) {
			CAPI.MATERIALS.getItem(tag, material).ifPresent(item -> this.item(tag, marker, item));
		}
	}
}
