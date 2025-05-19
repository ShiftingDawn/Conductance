package conductance.api.plugin;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RemoveRecipeEvent implements IConductancePluginEvent {

	private final Consumer<ResourceLocation> delegate;

	public void remove(final ResourceLocation resourceLocation) {
		this.delegate.accept(resourceLocation);
	}

	public void remove(final String resourcePath) {
		this.remove(ResourceLocation.withDefaultNamespace(resourcePath));
	}
}
