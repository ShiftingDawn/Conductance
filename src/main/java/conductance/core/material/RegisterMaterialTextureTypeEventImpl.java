package conductance.core.material;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.event.RegisterMaterialTextureTypeEvent;

@AllArgsConstructor
final class RegisterMaterialTextureTypeEventImpl implements RegisterMaterialTextureTypeEvent {

	private final Function<String, MaterialTextureType> delegate;

	@Override
	public MaterialTextureType register(final String name) {
		return this.delegate.apply(name);
	}
}
