package conductance.api.resource.event;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidType;
import conductance.api.material.Material;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.tier.Tier;

public interface AddTranslationEvent extends IConductancePluginEvent {

	void add(String key, String value);

	default void add(final Block block, final String value) {
		this.add(block.getDescriptionId(), value);
	}

	default void add(final Item item, final String value) {
		this.add(item.getDescriptionId(), value);
	}

	default void add(final FluidType fluid, final String value) {
		this.add(fluid.getDescriptionId(), value);
	}

	default void add(final Material material, final String value) {
		this.add(material.getDescriptionId(), value);
	}

	default void add(final Tier tier, final String value) {
		this.add(tier.getDescriptionId(), value);
	}

	default void add(final TagKey<?> tag, final String value) {
		this.add("tag.%s.%s.%s".formatted(
				tag.registry().location().getPath(),
				tag.location().getNamespace(),
				tag.location().getPath().replace('/', '.')
		), value);
	}
}
