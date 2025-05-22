package conductance.core.apiimpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.Conductance;

//TODO refactor
public final class MaterialFlagImpl {

	public static Set<MaterialFlag> verify(final Material material, final MaterialFlag flag) {
		flag.getRequiredTraits().forEach(trait -> {
			if (!material.hasTrait(trait)) {
				Conductance.LOGGER.warn("Material {} does not have required trait {} for flag {}}", material.getRegistryKey(), trait.getRegistryKey(), flag.getRegistryKey());
			}
		});
		final Set<MaterialFlag> resultSet = new HashSet<>(flag.getRequiredFlags());
		resultSet.addAll(flag.getRequiredFlags().stream().map(f -> MaterialFlagImpl.verify(material, f)).flatMap(Collection::stream).collect(Collectors.toSet()));
		return resultSet;
	}
}
