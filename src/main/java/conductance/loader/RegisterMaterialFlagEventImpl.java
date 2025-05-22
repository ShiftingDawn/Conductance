package conductance.loader;

import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.plugin.RegisterMaterialFlagEvent;

@AllArgsConstructor
final class RegisterMaterialFlagEventImpl implements RegisterMaterialFlagEvent {

	private final String modid;
	private final TriFunction<ResourceLocation, Set<MaterialFlag>, Set<MaterialTraitKey<?>>, MaterialFlag> delegate;

	@Override
	public MaterialFlag register(final String name, final Set<MaterialFlag> requiredFlags, final Set<MaterialTraitKey<?>> requiredTraits) {
		return this.delegate.apply(ResourceLocation.fromNamespaceAndPath(this.modid, name), requiredFlags, requiredTraits);
	}
}
