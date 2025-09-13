package conductance.core.material;

import java.util.Collection;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import conductance.api.material.MaterialFlag;
import conductance.api.material.event.RegisterMaterialFlagEvent;

@RequiredArgsConstructor
final class RegisterMaterialFlagEventImpl implements RegisterMaterialFlagEvent {

	private final BiFunction<String, Collection<MaterialFlag>, MaterialFlag> delegate;

	@Override
	public MaterialFlag register(final String registryName, final Collection<MaterialFlag> requiredFlags) {
		return this.delegate.apply(registryName, requiredFlags);
	}
}
