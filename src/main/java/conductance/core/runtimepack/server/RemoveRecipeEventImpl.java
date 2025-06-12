package conductance.core.runtimepack.server;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.recipe.event.RemoveRecipeEvent;

@AllArgsConstructor
final class RemoveRecipeEventImpl implements RemoveRecipeEvent {

	private final Consumer<ResourceLocation> delegate;

	@Override
	public void remove(final ResourceLocation resourceLocation) {
		this.delegate.accept(resourceLocation);
	}
}
