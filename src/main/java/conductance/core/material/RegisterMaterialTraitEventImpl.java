package conductance.core.material;

import lombok.AllArgsConstructor;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.RegisterMaterialTraitEvent;

@AllArgsConstructor
final class RegisterMaterialTraitEventImpl implements RegisterMaterialTraitEvent {

	public interface MaterialTraitRegister {

		<T extends IMaterialTrait<T>> MaterialTraitKey<T> apply(String registryName, Class<T> typeClass);
	}

	private final MaterialTraitRegister delegate;

	@Override
	public <T extends IMaterialTrait<T>> MaterialTraitKey<T> register(final String registryName, final Class<T> typeClass) {
		return this.delegate.apply(registryName, typeClass);
	}
}
