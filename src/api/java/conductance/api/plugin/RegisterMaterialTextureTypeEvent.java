package conductance.api.plugin;

import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.material.MaterialTextureType;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterMaterialTextureTypeEvent implements IConductancePluginEvent {

	private final Function<String, MaterialTextureType> delegate;

	public MaterialTextureType register(final String name) {
		return this.delegate.apply(name);
	}
}
