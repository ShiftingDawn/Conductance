package conductance.core.material;

import conductance.api.CAPI;

final class MaterialValidator {

	public static void validateMaterials() {
		CAPI.regs().materials().forEach(material -> MaterialValidator.validateMaterial((MaterialImpl) material));
	}

	private static void validateMaterial(final MaterialImpl material) {
		material.getTraits().forEach((key, trait) -> {
			trait.validate(material, assertKey -> {
				if (!material.has(key)) {
					throw new IllegalStateException("Material %s is missing trait %s but it is required by trait %s"
							.formatted(material.getRegistryKey(), assertKey.getRegistryKey(), key.getRegistryKey()));
				}
			});
		});
		material.getFlags().forEach(flag -> {
			flag.getRequiredTraits().forEach(requiredTrait -> {
				if (!material.has(requiredTrait)) {
					throw new IllegalStateException("Material %s is missing trait %s but it is required by flag %s"
							.formatted(material.getRegistryKey(), requiredTrait.getRegistryKey(), flag.getRegistryKey()));
				}
			});
			flag.getRequiredFlags().forEach(requiredFlag -> {
				if (!material.has(requiredFlag)) {
					throw new IllegalStateException("Material %s is missing flag %s but it is required by flag %s"
							.formatted(material.getRegistryKey(), requiredFlag.getRegistryKey(), flag.getRegistryKey()));
				}
			});
		});
	}

	private MaterialValidator() {
	}
}
