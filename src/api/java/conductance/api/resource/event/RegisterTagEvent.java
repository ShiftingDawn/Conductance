package conductance.api.resource.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterTagEvent extends IConductancePluginEvent {

	void item(TagKey<Item> tag, ItemLike value, ItemLike... moreValues);

	void tag(TagKey<Item> tag, ResourceLocation value, ResourceLocation... moreValues);

	void optionalTag(TagKey<Item> tag, ResourceLocation value, ResourceLocation... moreValues);
}
