package conductance.core.material;

import lombok.RequiredArgsConstructor;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.RegisterMaterialTraitEvent;

@RequiredArgsConstructor
final class RegisterMaterialTraitEventImpl implements RegisterMaterialTraitEvent {

	public interface MaterialTraitRegister {

		<T extends MaterialTrait<T>> MaterialTraitKey<T> apply(String registryName, Class<T> typeClass);
	}

	private final MaterialTraitRegister delegate;

	@Override
	public <T extends MaterialTrait<T>> MaterialTraitKey<T> register(final String registryName, final Class<T> typeClass) {
		return this.delegate.apply(registryName, typeClass);
	}
}
