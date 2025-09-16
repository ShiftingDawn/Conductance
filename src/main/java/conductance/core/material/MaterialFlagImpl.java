package conductance.core.material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialFlagValidator;

final class MaterialFlagImpl implements MaterialFlag {

	private final Set<MaterialFlag> requiredFlags;
	private final @Nullable MaterialFlagValidator validator;

	MaterialFlagImpl(final Collection<MaterialFlag> requiredFlags, @Nullable final MaterialFlagValidator validator) {
		this.requiredFlags = Set.copyOf(requiredFlags);
		this.validator = validator;
	}

	List<String> validate(final Material material) {
		final List<String> errors = new ArrayList<>();
		for (final MaterialFlag requiredFlag : this.requiredFlags) {
			if (!material.hasFlag(requiredFlag)) {
				errors.add("Missing required flag " + requiredFlag.getId());
			}
		}
		if (this.validator != null) {
			final List<String> validation = this.validator.validate(material);
			if (validation != null) {
				errors.addAll(validation);
			}
		}
		return errors;
	}
}
