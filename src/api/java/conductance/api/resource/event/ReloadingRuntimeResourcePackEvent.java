package conductance.api.resource.event;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonElement;
import conductance.api.plugin.IConductancePluginEvent;

public interface ReloadingRuntimeResourcePackEvent extends IConductancePluginEvent {

	void addBlockState(ResourceLocation location, JsonElement data);

	default void addBlockState(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.addBlockState(location, provider.get());
	}

	void addBlockModel(ResourceLocation location, JsonElement data);

	default void addBlockModel(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.addBlockModel(location, provider.get());
	}

	void addItemModel(ResourceLocation location, JsonElement data);

	default void addItemModel(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.addItemModel(location, provider.get());
	}
}
