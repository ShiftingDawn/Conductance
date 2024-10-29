package conductance.api.material;

import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import conductance.api.registry.RegistryObject;

public final class MaterialTraitKey<T extends IMaterialTrait<T>> extends RegistryObject<ResourceLocation> {

	@Getter
	private final Class<T> typeClass;

	public MaterialTraitKey(ResourceLocation registryKey, Class<T> typeClass) {
		super(registryKey);
		this.typeClass = typeClass;
	}
}
