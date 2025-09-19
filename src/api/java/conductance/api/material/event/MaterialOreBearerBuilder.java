package conductance.api.material.event;

import conductance.api.material.MaterialOreBearer;

public interface MaterialOreBearerBuilder {

	MaterialOreBearerBuilder blockType(MaterialOreBearer.BlockType type);

	MaterialOreBearerBuilder doubleOutput();

	MaterialOreBearerBuilder hasGravity();
}
