package conductance.core.apiimpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;

public final class MaterialFlagMap {

	private final Set<MaterialFlag> flags = new HashSet<>();

	public MaterialFlagMap add(final MaterialFlag... flagsToAdd) {
		this.flags.addAll(Arrays.asList(flagsToAdd));
		return this;
	}

	void verify(final Material material) {
		this.flags.addAll(this.flags.stream().map(flag -> ((MaterialFlagImpl) flag).verify(material)).flatMap(Collection::stream).collect(Collectors.toSet()));
	}

	public boolean has(final MaterialFlag flag) {
		return this.flags.contains(flag);
	}
}
