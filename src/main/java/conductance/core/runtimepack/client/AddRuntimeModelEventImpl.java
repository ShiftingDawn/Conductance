package conductance.core.runtimepack.client;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;

@AllArgsConstructor
final class AddRuntimeModelEventImpl implements AddRuntimeModelEvent {

	private final BiConsumer<ResourceLocation, Consumer<BlockStateBuilder>> addBlockStateDelegate;
	private final BiConsumer<ResourceLocation, Consumer<ModelBuilder>> addBlockModelDelegate;
	private final BiConsumer<ResourceLocation, Consumer<ModelBuilder>> addItemModelDelegate;
	private final BiConsumer<ResourceLocation, JsonElement> insertBlockStateDelegate;
	private final BiConsumer<ResourceLocation, JsonElement> insertBlockModelDelegate;
	private final BiConsumer<ResourceLocation, JsonElement> insertItemModelDelegate;

	@Override
	public void addBlockState(final ResourceLocation location, final Consumer<BlockStateBuilder> builder) {
		this.addBlockStateDelegate.accept(location, builder);
	}

	@Override
	public void addBlockModel(final ResourceLocation location, final Consumer<ModelBuilder> builder) {
		this.addBlockModelDelegate.accept(location, builder);
	}

	@Override
	public void addItemModel(final ResourceLocation location, final Consumer<ModelBuilder> builder) {
		this.addItemModelDelegate.accept(location, builder);
	}

	@Override
	public void insertBlockState(final ResourceLocation location, final JsonElement data) {
		this.insertBlockStateDelegate.accept(location, data);
	}

	@Override
	public void insertBlockModel(final ResourceLocation location, final JsonElement data) {
		this.insertBlockModelDelegate.accept(location, data);
	}

	@Override
	public void insertItemModel(final ResourceLocation location, final JsonElement data) {
		this.insertItemModelDelegate.accept(location, data);
	}
}
