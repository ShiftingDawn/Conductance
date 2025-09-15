package conductance.api.material.event;

import net.minecraft.resources.ResourceLocation;

public interface MaterialBuilder {

	MaterialBuilder dust();

	MaterialBuilder ingot();

	MaterialBuilder gem();

	MaterialBuilder textureSet(ResourceLocation textureSet);
}
