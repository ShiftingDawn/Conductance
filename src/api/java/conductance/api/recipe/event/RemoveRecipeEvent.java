package conductance.api.recipe.event;

import net.minecraft.resources.ResourceLocation;
import conductance.api.plugin.IConductancePluginEvent;

public interface RemoveRecipeEvent extends IConductancePluginEvent {

	void remove(ResourceLocation resourceLocation);

	default void remove(final String resourcePath) {
		this.remove(ResourceLocation.withDefaultNamespace(resourcePath));
	}
}
