package conductance.core.apiimpl;

import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import lombok.experimental.Accessors;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.registry.ConductanceRegistry;
import conductance.api.registry.IRegistryObject;
import conductance.api.registry.RegistryProvider;
import conductance.Conductance;

@Getter
@Accessors(fluent = true)
public final class RegistryProviderImpl implements RegistryProvider {

	private final ConductanceRegistry<ResourceLocation, PeriodicElement> periodicElements = makeResourceKeyed("periodic_element");

	private final ConductanceRegistry<ResourceLocation, MaterialTextureType> materialTextureTypes = makeResourceKeyed("material_texture_type");
	private final ConductanceRegistry<String, MaterialTextureSet> materialTextureSets = makeStringKeyed("material_texture_set");
	private final ConductanceRegistry<ResourceLocation, MaterialTraitKey<?>> materialTraits = makeResourceKeyed("material_traits");
	private final ConductanceRegistry<ResourceLocation, MaterialFlag> materialFlags = makeResourceKeyed("material_flags");
	private final ConductanceRegistry<ResourceLocation, Material> materials = makeResourceKeyed("material");

	RegistryProviderImpl() {
	}

	private static <VALUE extends IRegistryObject<String>> ConductanceRegistryImpl<String, VALUE> makeStringKeyed(final String registryName) {
		return new ConductanceRegistryImpl.StringKeyed<>(Conductance.id(registryName));
	}

	private static <VALUE extends IRegistryObject<ResourceLocation>> ConductanceRegistryImpl<ResourceLocation, VALUE> makeResourceKeyed(final String registryName) {
		return new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id(registryName));
	}

	private static <VALUE extends IRegistryObject<ResourceLocation>> ConductanceDataPackRegistry<VALUE> makeDataPack(final String registryName) {
		return new ConductanceDataPackRegistry<>(Conductance.id(registryName));
	}
}
