package conductance.api.plugin;

import net.minecraft.resources.ResourceLocation;

public interface RemoveRecipeEvent extends IConductancePluginEvent {

	void remove(ResourceLocation resourceLocation);

	default void remove(final String resourcePath) {
		this.remove(ResourceLocation.withDefaultNamespace(resourcePath));
	}
}
