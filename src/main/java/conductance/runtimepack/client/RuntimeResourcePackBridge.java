package conductance.runtimepack.client;

import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import conductance.api.resource.BlockModelBuilder;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.ItemModelBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.core.machine.BlockModelBuilderImpl;
import conductance.core.machine.BlockStateBuilderImpl;
import conductance.core.machine.ItemModelBuilderImpl;
import conductance.loader.PluginEventBus;

public final class RuntimeResourcePackBridge {

	public static void reset() {
		RuntimeResourcePack.reset();
	}

	public static void loadModels() {
		//TODO reset models
		PluginEventBus.postAll(AddRuntimeModelEvent.class, new AddRuntimeModelEventImpl(
				RuntimeResourcePackBridge::addBlockState, RuntimeResourcePackBridge::addBlockModel, RuntimeResourcePackBridge::addItemModel,
				RuntimeResourcePack::addBlockState, RuntimeResourcePack::addBlockModel, RuntimeResourcePack::addItemModel
		));
	}

	public static void loadTranslations() {
		PluginEventBus.postAll(AddTranslationEvent.class, new AddTranslationEventImpl(RuntimeResourcePack::addTranslation));
		RuntimeResourcePack.freezeTranslations();
	}

	private static void addBlockState(final ResourceLocation location, final Consumer<BlockStateBuilder> builder) {
		final JsonObject data = Util.make(new BlockStateBuilderImpl(), builder).build();
		RuntimeResourcePack.addBlockState(location, data);
	}

	private static void addBlockModel(final ResourceLocation location, final Consumer<BlockModelBuilder<?>> builder) {
		final JsonObject data = Util.make(new BlockModelBuilderImpl(), builder).build();
		RuntimeResourcePack.addBlockModel(location, data);
	}

	private static void addItemModel(final ResourceLocation location, final Consumer<ItemModelBuilder<?>> builder) {
		final JsonObject data = Util.make(new ItemModelBuilderImpl(), builder).build();
		RuntimeResourcePack.addItemModel(location, data);
	}

	private RuntimeResourcePackBridge() {
	}
}
