package conductance.core.block;

import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public interface IMaterialBlock {

	Material getMaterial();

	MaterialGenerationHandler getHandler();
}
