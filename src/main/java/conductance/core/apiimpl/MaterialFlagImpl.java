package conductance.core.apiimpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.ImmutableSet;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.registry.RegistryObject;
import conductance.Conductance;

public final class MaterialFlagImpl extends RegistryObject<ResourceLocation> implements MaterialFlag {

	private final Set<MaterialFlagImpl> requiredFlags;
	private final Set<MaterialTraitKey<?>> requiredTraits;

	public MaterialFlagImpl(ResourceLocation registryKey, final Set<MaterialFlagImpl> requiredFlags, final Set<MaterialTraitKey<?>> requiredTraits) {
		super(registryKey);
		this.requiredFlags = requiredFlags;
		this.requiredTraits = requiredTraits;
	}

	public Set<MaterialFlagImpl> verify(final Material material) {
		this.requiredTraits.forEach(typeKey -> {
			if (!material.hasTrait(typeKey)) {
				Conductance.LOGGER.warn("Material {} does not have required trait {} for flag {}}", material.getRegistryKey(), typeKey.getRegistryKey(), this.getRegistryKey());
			}
		});
		final Set<MaterialFlagImpl> resultSet = new HashSet<>(this.requiredFlags);
		resultSet.addAll(this.requiredFlags.stream().map(f -> f.verify(material)).flatMap(Collection::stream).collect(Collectors.toSet()));
		return resultSet;
	}

	public static class Builder {

		private final ResourceLocation registryName;

		private final Set<MaterialFlagImpl> requiredFlags = new HashSet<>();
		private final Set<MaterialTraitKey<?>> requiredTraits = new HashSet<>();

		public Builder(final ResourceLocation registryName) {
			this.registryName = registryName;
		}

		public Builder requiredFlag(final Set<MaterialFlag> flags) {
			this.requiredFlags.addAll(flags.stream().map(flag -> (MaterialFlagImpl) flag).toList());
			return this;
		}

		public Builder requiredTrait(final Set<MaterialTraitKey<?>> typeKeys) {
			this.requiredTraits.addAll(typeKeys);
			return this;
		}

		public MaterialFlag build() {
			return new MaterialFlagImpl(this.registryName, ImmutableSet.copyOf(this.requiredFlags), ImmutableSet.copyOf(this.requiredTraits));
		}
	}
}
