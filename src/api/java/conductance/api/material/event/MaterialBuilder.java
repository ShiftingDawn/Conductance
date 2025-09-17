package conductance.api.material.event;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import conductance.api.NCMaterialFlags;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;

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

	<T extends MaterialTrait<T>> MaterialBuilder trait(MaterialTraitKey<T> key, T instance);

	MaterialBuilder liquid(Supplier<MaterialTraitFluid.Liquid> factory);

	default MaterialBuilder liquid(final int temperature) {
		return this.liquid(() -> new MaterialTraitFluid.Liquid(-1, temperature, -1));
	}

	default MaterialBuilder liquid() {
		return this.liquid(-1);
	}

	MaterialBuilder gas(Supplier<MaterialTraitFluid.Gas> factory);

	default MaterialBuilder gas(final int temperature) {
		return this.gas(() -> new MaterialTraitFluid.Gas(-1, temperature, -1));
	}

	default MaterialBuilder gas() {
		return this.gas(-1);
	}

	MaterialBuilder plasma(Supplier<MaterialTraitFluid.Plasma> factory);

	default MaterialBuilder plasma(final int temperature) {
		return this.plasma(() -> new MaterialTraitFluid.Plasma(-1, temperature, -1));
	}

	default MaterialBuilder plasma() {
		return this.plasma(-1);
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
