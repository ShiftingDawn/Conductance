package conductance.lib.pack.server;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import lombok.AllArgsConstructor;
import conductance.api.resource.event.RegisterTagEvent;

@AllArgsConstructor
final class RegisterTagEventImpl implements RegisterTagEvent {

	interface TagRegister {

		void item(TagKey<Item> tag, ItemLike value, ItemLike... moreValues);

		void tag(TagKey<Item> tag, ResourceLocation value, ResourceLocation... moreValues);

		void optionalTag(TagKey<Item> tag, ResourceLocation value, ResourceLocation... moreValues);
	}

	private final TagRegister delegate;

	@Override
	public void item(final TagKey<Item> tag, final ItemLike value, final ItemLike... moreValues) {
		this.delegate.item(tag, value, moreValues);
	}

	@Override
	public void tag(final TagKey<Item> tag, final ResourceLocation value, final ResourceLocation... moreValues) {
		this.delegate.tag(tag, value, moreValues);
	}

	@Override
	public void optionalTag(final TagKey<Item> tag, final ResourceLocation value, final ResourceLocation... moreValues) {
		this.delegate.optionalTag(tag, value, moreValues);
	}
}
