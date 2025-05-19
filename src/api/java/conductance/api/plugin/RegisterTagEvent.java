package conductance.api.plugin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.util.Marker;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterTagEvent implements IConductancePluginEvent {

	public interface TagRegister {

		void item(TagKey<Item> tag, ItemLike value, ItemLike... moreValues);

		<MARKER extends Material & Marker> void item(TaggedMaterialSet tag, MARKER marker, ItemLike value, ItemLike... moreValues);

		<MARKER extends Material & Marker> void item(TaggedMaterialSet tag, MARKER marker, Material value, Material... moreValues);
	}

	private final TagRegister delegate;

	public void item(final TagKey<Item> tag, final ItemLike value, final ItemLike... moreValues) {
		this.delegate.item(tag, value, moreValues);
	}

	public <MARKER extends Material & Marker> void item(final TaggedMaterialSet tag, final MARKER marker, final ItemLike value, final ItemLike... moreValues) {
		this.delegate.item(tag, marker, value, moreValues);
	}

	public <MARKER extends Material & Marker> void item(final TaggedMaterialSet tag, final MARKER marker, final Material value, final Material... moreValues) {
		this.delegate.item(tag, marker, value, moreValues);
	}
}
