package conductance.core.material;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialFlagValidator;
import conductance.api.material.event.RegisterMaterialFlagEvent;

@RequiredArgsConstructor
final class RegisterMaterialFlagEventImpl implements RegisterMaterialFlagEvent {

	private final TriFunction<String, Collection<MaterialFlag>, @Nullable MaterialFlagValidator, MaterialFlag> delegate;

	@Override
	public MaterialFlag register(final String registryName, final Collection<MaterialFlag> requiredFlags, @Nullable final MaterialFlagValidator validator) {
		return this.delegate.apply(registryName, requiredFlags, validator);
	}
}
