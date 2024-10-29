package conductance.api.plugin;

import java.util.function.Function;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSetBuilder;
import conductance.api.util.TextHelper;

public interface MaterialTaggedSetRegister {

	TaggedMaterialSetBuilder register(String registryKey, final Function<Material, String> unlocalizedNameFactory);

	default TaggedMaterialSetBuilder register(String registryKey, final String unlocalizedNameFactory) {
		return this.register(registryKey, ignored -> unlocalizedNameFactory);
	}

	default TaggedMaterialSetBuilder register(String registryKey) {
		return this.register(registryKey, "%s_" + TextHelper.toLowerCaseUnderscore(registryKey));
	}
}
