package conductance.api.resource;

import net.minecraft.world.level.block.state.properties.Property;

public interface BlockStateVariantsBuilder {

	<T extends Comparable<T>> BlockStateVariantBuilder variant(Property<T> property, T value);
}
