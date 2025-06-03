package conductance.api.material;

import java.util.Collections;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import conductance.api.registry.RegistryObject;

@Getter
public final class MaterialFlag extends RegistryObject<ResourceLocation> {

	private final Set<MaterialFlag> requiredFlags;
	private final Set<MaterialTraitKey<?>> requiredTraits;

	public MaterialFlag(final ResourceLocation registryKey, final Set<MaterialFlag> requiredFlags, final Set<MaterialTraitKey<?>> requiredTraits) {
		super(registryKey);
		this.requiredFlags = Collections.unmodifiableSet(requiredFlags);
		this.requiredTraits = Collections.unmodifiableSet(requiredTraits);
	}
}
