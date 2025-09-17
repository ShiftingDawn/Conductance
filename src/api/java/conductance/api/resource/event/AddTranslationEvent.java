package conductance.api.resource.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import conductance.api.material.Material;
import conductance.api.plugin.IConductancePluginEvent;

public interface AddTranslationEvent extends IConductancePluginEvent {

	void add(String key, String value);

	default void add(final Block block, final String value) {
		this.add(block.getDescriptionId(), value);
	}

	default void add(final Item item, final String value) {
		this.add(item.getDescriptionId(), value);
	}

	default void add(final Material material, final String value) {
		this.add(material.getDescriptionId(), value);
	}

	default void add(final TagKey<?> tag, final String value) {
		this.add("tag.%s.%s.%s".formatted(
				tag.registry().location().getPath(),
				tag.location().getNamespace(),
				tag.location().getPath().replace('/', '.')
		), value);
	}
}
