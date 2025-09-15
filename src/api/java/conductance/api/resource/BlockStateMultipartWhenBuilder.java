package conductance.api.resource;

import net.minecraft.world.level.block.state.properties.Property;

public interface BlockStateMultipartWhenBuilder {

	<T extends Comparable<T>> BlockStateMultipartWhenBuilder when(Property<T> property, T value);
}
