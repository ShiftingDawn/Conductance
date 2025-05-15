package conductance.api.plugin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.material.MaterialTextureSet;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterMaterialTextureSetEvent implements IConductancePluginEvent {

	public interface MaterialTextureSetRegister {

		MaterialTextureSet register(String name, String parentSet);
	}

	private final MaterialTextureSetRegister delegate;

	public MaterialTextureSet register(final String name, final String parentSet) {
		return this.delegate.register(name, parentSet);
	}

	public MaterialTextureSet register(final String name, final MaterialTextureSet parent) {
		return this.register(name, parent.getRegistryKey());
	}

	public MaterialTextureSet register(final String name) {
		return this.register(name, "dull");
	}
}
