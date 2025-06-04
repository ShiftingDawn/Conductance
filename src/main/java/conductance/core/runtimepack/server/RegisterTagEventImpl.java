package conductance.core.runtimepack.server;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import lombok.AllArgsConstructor;
import conductance.api.CAPI;
import conductance.api.machine.recipe.RecipeHelper;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.RegisterTagEvent;
import conductance.api.util.Marker;

@AllArgsConstructor
final class RegisterTagEventImpl implements RegisterTagEvent {

	interface TagRegister {
		void item(TagKey<Item> tag, ItemLike value, ItemLike... moreValues);
	}

	private final TagRegister delegate;

	@Override
	public void item(final TagKey<Item> tag, final ItemLike value, final ItemLike... moreValues) {
		this.delegate.item(tag, value, moreValues);
	}

	@Override
	public <MARKER extends Material & Marker> void item(final TaggedMaterialSet tag, final MARKER marker, final ItemLike value, final ItemLike... moreValues) {
		final TagKey<Item> tagKey = RecipeHelper.getItemTag(tag, marker);
		if (tagKey != null) {
			this.item(tagKey, value, moreValues);
		}
	}

	@Override
	public <MARKER extends Material & Marker> void item(final TaggedMaterialSet tag, final MARKER marker, final Material value, final Material... moreValues) {
		CAPI.materials().getItem(tag, value).ifPresent(item -> this.item(tag, marker, item));
		for (final Material material : moreValues) {
			CAPI.materials().getItem(tag, material).ifPresent(item -> this.item(tag, marker, item));
		}
	}
}
