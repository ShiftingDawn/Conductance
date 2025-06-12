package conductance.lib.mixinext;

import javax.annotation.Nullable;
import net.minecraft.core.Registry;

public interface MixinTagLoaderExtension<T> {

	void conductance$setRegistry(@Nullable Registry<T> registry);

	@Nullable
	Registry<T> conductance$getRegistry();
}
