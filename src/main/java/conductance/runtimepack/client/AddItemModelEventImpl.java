package conductance.runtimepack.client;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.resource.ItemModelBuilder;
import conductance.api.resource.event.AddItemModelEvent;

@AllArgsConstructor
final class AddItemModelEventImpl implements AddItemModelEvent {

	private final BiConsumer<ResourceLocation, Consumer<ItemModelBuilder<?>>> delegate;

	@Override
	public void add(final ResourceLocation location, final Consumer<ItemModelBuilder<?>> builder) {
		this.delegate.accept(location, builder);
	}
}
