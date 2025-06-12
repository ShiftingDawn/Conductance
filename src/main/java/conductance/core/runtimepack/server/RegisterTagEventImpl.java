package conductance.core.runtimepack.server;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import lombok.AllArgsConstructor;
import conductance.api.resource.RegisterTagEvent;

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
}
