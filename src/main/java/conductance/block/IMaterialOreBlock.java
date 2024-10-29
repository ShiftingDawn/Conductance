package conductance.block;

import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;

public interface IMaterialOreBlock {

	Material getMaterial();

	MaterialOreType getOreType();
}
