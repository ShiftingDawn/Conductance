package conductance.runtimepack.client;

import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import conductance.api.resource.event.ReloadingRuntimeResourcePackEvent;

@AllArgsConstructor
final class ReloadingRuntimeResourcePackEventImpl implements ReloadingRuntimeResourcePackEvent {

	private final BiConsumer<ResourceLocation, JsonElement> blockStateDelegate;
	private final BiConsumer<ResourceLocation, JsonElement> blockModelDelegate;
	private final BiConsumer<ResourceLocation, JsonElement> itemModelDelegate;

	@Override
	public void addBlockState(final ResourceLocation location, final JsonElement provider) {
		this.blockStateDelegate.accept(location, provider);
	}

	@Override
	public void addBlockModel(final ResourceLocation location, final JsonElement provider) {
		this.blockModelDelegate.accept(location, provider);

	}

	@Override
	public void addItemModel(final ResourceLocation location, final JsonElement provider) {
		this.itemModelDelegate.accept(location, provider);

	}
}
