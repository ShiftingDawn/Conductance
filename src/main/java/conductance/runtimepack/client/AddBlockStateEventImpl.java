package conductance.runtimepack.client;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.event.AddBlockStateEvent;

@AllArgsConstructor
final class AddBlockStateEventImpl implements AddBlockStateEvent {

	private final BiConsumer<ResourceLocation, Consumer<BlockStateBuilder>> delegate;

	@Override
	public void add(final ResourceLocation location, final Consumer<BlockStateBuilder> builder) {
		this.delegate.accept(location, builder);
	}
}
