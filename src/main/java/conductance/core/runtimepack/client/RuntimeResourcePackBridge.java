package conductance.core.runtimepack.client;

import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.ModelBuilder;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.api.resource.event.AddTranslationEvent;
import conductance.Conductance;

public final class RuntimeResourcePackBridge {

	public static void loadModels() {
		final long startTime = System.currentTimeMillis();
		RuntimeResourcePack.resetStatesAndModels();
		Conductance.dispatchAll(AddRuntimeModelEvent.class, new AddRuntimeModelEventImpl(
				RuntimeResourcePackBridge::addBlockState, RuntimeResourcePackBridge::addBlockModel, RuntimeResourcePackBridge::addItemModel,
				RuntimeResourcePack::addBlockState, RuntimeResourcePack::addBlockModel, RuntimeResourcePack::addItemModel
		));
		Conductance.LOGGER.info("Conductance loaded RuntimeResourcePack blockstates and models in {}ms", System.currentTimeMillis() - startTime);
	}

	public static void loadTranslations() {
		final long startTime = System.currentTimeMillis();
		RuntimeResourcePack.resetTranslations();
		Conductance.dispatchAll(AddTranslationEvent.class, new AddTranslationEventImpl(RuntimeResourcePack::addTranslation));
		RuntimeResourcePack.freezeTranslations();
		Conductance.LOGGER.info("Conductance loaded RuntimeResourcePack translations in {}ms", System.currentTimeMillis() - startTime);
	}

	private static void addBlockState(final ResourceLocation location, final Consumer<BlockStateBuilder> builder) {
		final JsonObject data = Util.make(new BlockStateBuilderImpl(), builder).build();
		RuntimeResourcePack.addBlockState(location, data);
	}

	private static void addBlockModel(final ResourceLocation location, final Consumer<ModelBuilder> builder) {
		final JsonObject data = Util.make(new ModelBuilderImpl(ResourceLocation.withDefaultNamespace("block/cube")), builder).build();
		RuntimeResourcePack.addBlockModel(location, data);
	}

	private static void addItemModel(final ResourceLocation location, final Consumer<ModelBuilder> builder) {
		final JsonObject data = Util.make(new ModelBuilderImpl(ResourceLocation.withDefaultNamespace("item/generated")), builder).build();
		RuntimeResourcePack.addItemModel(location, data);
	}

	private RuntimeResourcePackBridge() {
	}
}
