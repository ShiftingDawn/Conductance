package conductance.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.periodicelement.PeriodicElement;

public interface RegistryProvider {

	ResourceKey<Registry<PeriodicElement>> periodicElementRegistry();

	ResourceKey<Registry<MaterialFlag>> materialFlagRegistry();

	ResourceKey<Registry<Material>> materialRegistry();

	Registry<PeriodicElement> periodicElements();

	Registry<MaterialFlag> materialFlags();

	Registry<Material> materials();
}
