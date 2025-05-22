package conductance.loader;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.material.MaterialTextureType;
import conductance.api.plugin.RegisterMaterialTextureTypeEvent;

@AllArgsConstructor
final class RegisterMaterialTextureTypeEventImpl implements RegisterMaterialTextureTypeEvent {

	private final String modid;
	private final Function<ResourceLocation, MaterialTextureType> delegate;

	@Override
	public MaterialTextureType register(final String name) {
		return this.delegate.apply(ResourceLocation.fromNamespaceAndPath(this.modid, name));
	}
}
