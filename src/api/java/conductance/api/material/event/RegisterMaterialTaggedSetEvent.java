package conductance.api.material.event;

import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.material.TaggedMaterialSetBuilder;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.util.TextHelper;

public interface RegisterMaterialTaggedSetEvent extends IConductancePluginEvent {

	TaggedMaterialSet register(String registryKey, Function<Material, String> unlocalizedNameFactory, @Nullable MaterialOreType oreType, Consumer<TaggedMaterialSetBuilder> builder);

	default TaggedMaterialSet register(final String registryKey, final Function<Material, String> unlocalizedNameFactory, final Consumer<TaggedMaterialSetBuilder> builder) {
		return this.register(registryKey, unlocalizedNameFactory, null, builder);
	}

	default TaggedMaterialSet register(final String registryKey, final String unlocalizedNameFactory, @Nullable final MaterialOreType oreType, final Consumer<TaggedMaterialSetBuilder> builder) {
		return this.register(registryKey, ignored -> unlocalizedNameFactory, oreType, builder);
	}

	default TaggedMaterialSet register(final String registryKey, final String unlocalizedNameFactory, final Consumer<TaggedMaterialSetBuilder> builder) {
		return this.register(registryKey, ignored -> unlocalizedNameFactory, null, builder);
	}

	default TaggedMaterialSet register(final String registryKey, final Consumer<TaggedMaterialSetBuilder> builder) {
		return this.register(registryKey, "%s_" + TextHelper.toLowerCaseUnderscore(registryKey), builder);
	}
}
