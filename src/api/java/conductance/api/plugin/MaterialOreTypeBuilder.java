package conductance.api.plugin;

import conductance.api.material.MaterialOreType;

public interface MaterialOreTypeBuilder {

	MaterialOreTypeBuilder blockType(MaterialOreType.OreBlockType blockType);

	MaterialOreTypeBuilder doubleOutput();

	MaterialOreTypeBuilder hasGravity();

	MaterialOreType build();
}
