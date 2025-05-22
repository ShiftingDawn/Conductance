package conductance.core.material;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.TaggedMaterialSetBuilder;
import conductance.api.plugin.RegisterMaterialTaggedSetEvent;

@AllArgsConstructor
final class RegisterMaterialTaggedSetEventImpl implements RegisterMaterialTaggedSetEvent {

	interface MaterialTaggedSetRegister {

		TaggedMaterialSet apply(String registryName, Function<Material, String> unlocalizedNameFactory, @Nullable MaterialOreType oreType, Consumer<TaggedMaterialSetBuilder> builder);
	}

	private final MaterialTaggedSetRegister delegate;

	@Override
	public TaggedMaterialSet register(final String registryName, final Function<Material, String> unlocalizedNameFactory, @Nullable final MaterialOreType oreType, final Consumer<TaggedMaterialSetBuilder> builder) {
		return this.delegate.apply(registryName, unlocalizedNameFactory, oreType, builder);
	}
}
