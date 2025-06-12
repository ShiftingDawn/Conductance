package conductance.api.plugin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface RegisterTagEvent extends IConductancePluginEvent {

	void item(TagKey<Item> tag, ItemLike value, ItemLike... moreValues);
}
