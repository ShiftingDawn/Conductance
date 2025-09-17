package conductance.api.material.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import conductance.api.NCMaterialFlags;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;

public interface MaterialBuilder {

	MaterialBuilder flag(MaterialFlag flag);

	default MaterialBuilder dust() {
		return this.flag(NCMaterialFlags.DUST);
	}

	default MaterialBuilder ingot() {
		return this.flag(NCMaterialFlags.INGOT);
	}

	default MaterialBuilder gem() {
		return this.flag(NCMaterialFlags.GEM);
	}

	default MaterialBuilder block() {
		return this.flag(NCMaterialFlags.BLOCK);
	}

	default MaterialBuilder plate() {
		return this.flag(NCMaterialFlags.PLATE);
	}

	default MaterialBuilder rod() {
		return this.flag(NCMaterialFlags.ROD);
	}

	default MaterialBuilder gear() {
		return this.flag(NCMaterialFlags.GEAR);
	}

	default MaterialBuilder smallGear() {
		return this.flag(NCMaterialFlags.SMALL_GEAR);
	}

	default MaterialBuilder boltAndScrew() {
		return this.flag(NCMaterialFlags.BOLT_AND_SCREW);
	}

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
