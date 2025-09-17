package conductance.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialTraitKey;
import conductance.api.periodicelement.PeriodicElement;

public interface RegistryProvider {

	ResourceKey<Registry<PeriodicElement>> periodicElementRegistry();

	ResourceKey<Registry<MaterialFlag>> materialFlagRegistry();

	ResourceKey<Registry<Material>> materialRegistry();

	ResourceKey<Registry<MaterialGenerationHandler>> materialGenerationHandlerRegistry();

	ResourceKey<Registry<MaterialTraitKey<?>>> materialTraitRegistry();

	Registry<PeriodicElement> periodicElements();

	Registry<MaterialFlag> materialFlags();

	Registry<Material> materials();

	Registry<MaterialGenerationHandler> materialGenerationHandlers();

	Registry<MaterialTraitKey<?>> materialTraits();
}
