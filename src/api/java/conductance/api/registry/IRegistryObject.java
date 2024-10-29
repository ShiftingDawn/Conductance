package conductance.api.registry;

import org.jetbrains.annotations.NotNull;

public interface IRegistryObject<KEY> {

	@NotNull
	KEY getRegistryKey();
}
