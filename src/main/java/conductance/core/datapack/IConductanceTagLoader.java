package conductance.core.datapack;

import javax.annotation.Nullable;
import net.minecraft.core.Registry;

public interface IConductanceTagLoader<T> {

	void conductance$setRegistry(Registry<T> registry);

	@Nullable
	Registry<T> conductance$getRegistry();
}
