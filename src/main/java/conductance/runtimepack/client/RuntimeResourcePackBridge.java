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
import conductance.Conductance;
import conductance.core.machine.BlockModelBuilderImpl;
import conductance.core.machine.BlockStateBuilderImpl;
import conductance.core.machine.ItemModelBuilderImpl;
import conductance.loader.PluginEventBus;

public final class RuntimeResourcePackBridge {

	public static void loadModels() {
		final long startTime = System.currentTimeMillis();
		RuntimeResourcePack.resetStatesAndModels();
		PluginEventBus.postAll(AddRuntimeModelEvent.class, new AddRuntimeModelEventImpl(
				RuntimeResourcePackBridge::addBlockState, RuntimeResourcePackBridge::addBlockModel, RuntimeResourcePackBridge::addItemModel,
				RuntimeResourcePack::addBlockState, RuntimeResourcePack::addBlockModel, RuntimeResourcePack::addItemModel
		));
		Conductance.LOGGER.info("Conductance loaded RuntimeResourcePack blockstates and models in {}ms", System.currentTimeMillis() - startTime);
	}

	public static void loadTranslations() {
		final long startTime = System.currentTimeMillis();
		RuntimeResourcePack.resetTranslations();
		PluginEventBus.postAll(AddTranslationEvent.class, new AddTranslationEventImpl(RuntimeResourcePack::addTranslation));
		RuntimeResourcePack.freezeTranslations();
		Conductance.LOGGER.info("Conductance loaded RuntimeResourcePack translations in {}ms", System.currentTimeMillis() - startTime);
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
