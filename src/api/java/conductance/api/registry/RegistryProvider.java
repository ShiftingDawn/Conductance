package conductance.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import conductance.api.periodicelement.PeriodicElement;

public interface RegistryProvider {

	ResourceKey<Registry<PeriodicElement>> periodicElementRegistry();

	Registry<PeriodicElement> periodicElements();
}
