package conductance.loader;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.TaggedMaterialSetBuilder;
import conductance.api.plugin.RegisterMaterialTaggedSetEvent;

@AllArgsConstructor
final class RegisterMaterialTaggedSetEventImpl implements RegisterMaterialTaggedSetEvent {

	private final TriFunction<String, Function<Material, String>, MaterialOreType, TaggedMaterialSetBuilder> delegate;

	@Override
	public TaggedMaterialSet register(final String registryName, final Function<Material, String> unlocalizedNameFactory, @Nullable final MaterialOreType oreType, final Consumer<TaggedMaterialSetBuilder> builder) {
		//TODO refactor
		final TaggedMaterialSetBuilder b = this.delegate.apply(registryName, unlocalizedNameFactory, oreType);
		builder.accept(b);
		return b.build();
	}
}
