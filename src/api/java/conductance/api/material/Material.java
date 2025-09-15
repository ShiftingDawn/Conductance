package conductance.api.material;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;

public interface Material {

	boolean hasFlag(MaterialFlag flag);

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().materials().getKey(this), "Unregistered material");
	}

	default String getName() {
		return this.getId().getPath();
	}

	ResourceLocation getTextureSet();
}
