package conductance.api.resource;

import java.util.function.Consumer;
import net.minecraft.world.level.block.state.properties.Property;

public interface BlockStateVariantBuilder extends BlockStateModelDefiner {

	<T extends Comparable<T>> BlockStateVariantBuilder when(Property<T> property, T value);

	void models(Consumer<BlockStateModelDefiner> generatorConsumer);
}
