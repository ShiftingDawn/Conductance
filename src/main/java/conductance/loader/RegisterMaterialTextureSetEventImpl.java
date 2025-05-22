package conductance.loader;

import java.util.function.BiFunction;
import lombok.AllArgsConstructor;
import conductance.api.material.MaterialTextureSet;
import conductance.api.plugin.RegisterMaterialTextureSetEvent;

@AllArgsConstructor
final class RegisterMaterialTextureSetEventImpl implements RegisterMaterialTextureSetEvent {

	private final BiFunction<String, String, MaterialTextureSet> delegate;

	@Override
	public MaterialTextureSet register(final String name, final String parentSet) {
		return this.delegate.apply(name, parentSet);
	}

	@Override
	public MaterialTextureSet register(final String name, final MaterialTextureSet parent) {
		return this.register(name, parent.getRegistryKey());
	}

	@Override
	public MaterialTextureSet register(final String name) {
		return this.register(name, "dull");
	}
}
