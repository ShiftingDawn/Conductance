package conductance.runtimepack.client;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.resource.BlockModelBuilder;
import conductance.api.resource.event.AddBlockModelEvent;

@AllArgsConstructor
final class AddBlockModelEventImpl implements AddBlockModelEvent {

	private final BiConsumer<ResourceLocation, Consumer<BlockModelBuilder<?>>> delegate;

	@Override
	public void add(final ResourceLocation location, final Consumer<BlockModelBuilder<?>> builder) {
		this.delegate.accept(location, builder);
	}
}
