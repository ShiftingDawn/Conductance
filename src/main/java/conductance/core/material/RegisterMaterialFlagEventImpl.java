package conductance.core.material;

import java.util.Set;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.plugin.RegisterMaterialFlagEvent;

@AllArgsConstructor
final class RegisterMaterialFlagEventImpl implements RegisterMaterialFlagEvent {

	private final TriFunction<String, Set<MaterialFlag>, Set<MaterialTraitKey<?>>, MaterialFlag> delegate;

	@Override
	public MaterialFlag register(final String name, final Set<MaterialFlag> requiredFlags, final Set<MaterialTraitKey<?>> requiredTraits) {
		return this.delegate.apply(name, requiredFlags, requiredTraits);
	}
}
