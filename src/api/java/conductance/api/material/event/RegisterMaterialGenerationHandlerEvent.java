package conductance.api.material.event;

import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialOreBearer;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialGenerationHandlerEvent extends IConductancePluginEvent {

	MaterialGenerationHandler register(String registryName, Function<Material, String> unlocalizedNameFactory, @Nullable MaterialOreBearer oreBearer, Consumer<MaterialGenerationHandlerBuilder> builder);

	default MaterialGenerationHandler register(final String registryName, final Function<Material, String> unlocalizedNameFactory, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.register(registryName, unlocalizedNameFactory, null, builder);
	}

	default MaterialGenerationHandler register(final String registryName, final String unlocalizedNameFactory, @Nullable final MaterialOreBearer oreBearer, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.register(registryName, material -> unlocalizedNameFactory.formatted(material.getName()), oreBearer, builder);
	}

	default MaterialGenerationHandler register(final String registryName, final String unlocalizedNameFactory, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.register(registryName, unlocalizedNameFactory, null, builder);
	}

	default MaterialGenerationHandler register(final String registryName, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.register(registryName, "%s_" + registryName, builder);
	}
}
