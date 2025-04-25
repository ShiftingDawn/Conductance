package conductance.api.material;

import org.jetbrains.annotations.Nullable;
import conductance.api.registry.TaggedSet;

public interface TaggedMaterialSet extends TaggedSet<Material> {

	MaterialTextureType getTextureType();

	@Nullable
	MaterialOreType getOreType();
}
