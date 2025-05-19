package conductance.loader;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import lombok.AllArgsConstructor;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.RegisterTagEvent;
import conductance.api.util.Marker;
import conductance.runtimepack.server.TagRegister;

@AllArgsConstructor
final class RegisterTagEventImpl implements RegisterTagEvent {

	private final TagRegister delegate;

	@Override
	public void item(final TagKey<Item> tag, final ItemLike value, final ItemLike... moreValues) {
		this.delegate.item(tag, value, moreValues);
	}

	@Override
	public <MARKER extends Material & Marker> void item(final TaggedMaterialSet tag, final MARKER marker, final ItemLike value, final ItemLike... moreValues) {
		this.delegate.item(tag, marker, value, moreValues);
	}

	@Override
	public <MARKER extends Material & Marker> void item(final TaggedMaterialSet tag, final MARKER marker, final Material value, final Material... moreValues) {
		this.delegate.item(tag, marker, value, moreValues);
	}
}
