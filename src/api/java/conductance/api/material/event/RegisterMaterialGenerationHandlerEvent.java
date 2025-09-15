package conductance.api.material.event;

import java.util.function.Consumer;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialGenerationHandlerEvent extends IConductancePluginEvent {

	MaterialGenerationHandler register(String registryName, String unlocalizedNameFactory, Consumer<MaterialGenerationHandlerBuilder> builder);

	default MaterialGenerationHandler register(final String registryName, final Consumer<MaterialGenerationHandlerBuilder> builder) {
		return this.register(registryName, "%s_" + registryName, builder);
	}
}
