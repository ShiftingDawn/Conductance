package conductance.api.registry;

import net.minecraft.resources.ResourceLocation;
import conductance.api.material.PeriodicElement;

public interface RegistryProvider {

	ConductanceRegistry<ResourceLocation, PeriodicElement> periodicElements();
}
