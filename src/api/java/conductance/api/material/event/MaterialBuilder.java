package conductance.api.material.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import conductance.api.material.MaterialProp;

public interface MaterialBuilder {

	MaterialBuilder dust();

	MaterialBuilder ingot();

	MaterialBuilder gem();

	<T> MaterialBuilder prop(MaterialProp<T> property, T value);

	MaterialBuilder color(int rgb);

	default MaterialBuilder color(final int r, final int g, final int b) {
		return this.color(ARGB.color(r, g, b));
	}

	MaterialBuilder textureSet(ResourceLocation textureSet);

	default MaterialBuilder style(final int rgb, final ResourceLocation textureSet) {
		return this.color(rgb).textureSet(textureSet);
	}
}
