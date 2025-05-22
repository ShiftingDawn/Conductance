package conductance.loader;

import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.plugin.RegisterMaterialTraitEvent;

@AllArgsConstructor
final class RegisterMaterialTraitEventImpl implements RegisterMaterialTraitEvent {

	public interface MaterialTraitRegister {

		<T extends IMaterialTrait<T>> MaterialTraitKey<T> apply(ResourceLocation registryName, Class<T> typeClass);
	}

	private final String modid;
	private final MaterialTraitRegister delegate;

	@Override
	public <T extends IMaterialTrait<T>> MaterialTraitKey<T> register(final String registryName, final Class<T> typeClass) {
		return this.delegate.apply(ResourceLocation.fromNamespaceAndPath(this.modid, registryName), typeClass);
	}
}
