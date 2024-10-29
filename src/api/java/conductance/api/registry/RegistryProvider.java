package conductance.api.registry;

import net.minecraft.resources.ResourceLocation;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialOreType;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.material.TaggedMaterialSet;

public interface RegistryProvider {

	NCRegistrate registrate();

	ConductanceRegistry<ResourceLocation, PeriodicElement> periodicElements();

	ConductanceRegistry<String, MaterialTextureSet> materialTextureSets();

	ConductanceRegistry<ResourceLocation, MaterialTextureType> materialTextureTypes();

	ConductanceRegistry<ResourceLocation, MaterialTraitKey<?>> materialTraits();

	ConductanceRegistry<ResourceLocation, MaterialFlag> materialFlags();

	ConductanceRegistry<ResourceLocation, MaterialOreType> materialOreTypes();

	ConductanceRegistry<String, TaggedMaterialSet> materialTaggedSets();

	ConductanceRegistry<ResourceLocation, Material> materials();
}
