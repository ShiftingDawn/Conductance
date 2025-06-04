package conductance.api.resource.event;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonElement;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.resource.BlockModelBuilder;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.ItemModelBuilder;

public interface AddRuntimeModelEvent extends IConductancePluginEvent {

	void addBlockState(ResourceLocation location, Consumer<BlockStateBuilder> builder);

	void addBlockModel(ResourceLocation location, Consumer<BlockModelBuilder<?>> builder);

	void addItemModel(ResourceLocation location, Consumer<ItemModelBuilder<?>> builder);

	void insertBlockState(ResourceLocation location, JsonElement data);

	default void insertBlockState(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.insertBlockState(location, provider.get());
	}

	void insertBlockModel(ResourceLocation location, JsonElement data);

	default void insertBlockModel(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.insertBlockModel(location, provider.get());
	}

	void insertItemModel(ResourceLocation location, JsonElement data);

	default void insertItemModel(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.insertItemModel(location, provider.get());
	}
}
