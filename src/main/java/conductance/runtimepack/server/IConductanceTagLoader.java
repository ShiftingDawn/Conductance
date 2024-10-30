package conductance.runtimepack.server;

import javax.annotation.Nullable;
import net.minecraft.core.Registry;

public interface IConductanceTagLoader<T> {

	void conductance$setRegistry(@Nullable Registry<T> registry);

	@Nullable
	Registry<T> conductance$getRegistry();
}
