package conductance.api.plugin;

import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSetBuilder;
import conductance.api.util.TextHelper;

public interface MaterialTaggedSetRegister {

	TaggedMaterialSetBuilder register(String registryKey, Function<Material, String> unlocalizedNameFactory, @Nullable MaterialOreType oreType);

	default TaggedMaterialSetBuilder register(final String registryKey, final Function<Material, String> unlocalizedNameFactory) {
		return this.register(registryKey, unlocalizedNameFactory, null);
	}

	default TaggedMaterialSetBuilder register(final String registryKey, final String unlocalizedNameFactory, @Nullable final MaterialOreType oreType) {
		return this.register(registryKey, ignored -> unlocalizedNameFactory, oreType);
	}

	default TaggedMaterialSetBuilder register(final String registryKey, final String unlocalizedNameFactory) {
		return this.register(registryKey, ignored -> unlocalizedNameFactory, null);
	}

	default TaggedMaterialSetBuilder register(final String registryKey) {
		return this.register(registryKey, "%s_" + TextHelper.toLowerCaseUnderscore(registryKey));
	}
}
