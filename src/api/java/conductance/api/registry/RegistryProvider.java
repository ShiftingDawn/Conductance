package conductance.api.registry;

import net.minecraft.resources.ResourceLocation;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.PeriodicElement;

public interface RegistryProvider {

	ConductanceRegistry<ResourceLocation, PeriodicElement> periodicElements();

	ConductanceRegistry<String, MaterialTextureSet> materialTextureSets();

	ConductanceRegistry<ResourceLocation, MaterialTextureType> materialTextureTypes();
}
