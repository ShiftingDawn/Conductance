package conductance.api.material.event;

import java.util.function.Consumer;
import java.util.function.Function;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialGenerationHandlerEvent extends IConductancePluginEvent {

	MaterialGenerationHandler register(String registryName, Function<Material, String> unlocalizedNameFactory, Consumer<MaterialGenerationHandlerBuilder> builder);

	default MaterialGenerationHandler register(final String registryName, final String unlocalizedNameFactory, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.register(registryName, material -> unlocalizedNameFactory.formatted(material.getName()), builder);
	}

	default MaterialGenerationHandler register(final String registryName, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.register(registryName, "%s_" + registryName, builder);
	}
}
