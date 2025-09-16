package conductance.core.material;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTextureSets;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.event.MaterialBuilder;

final class MaterialBuilderImpl implements MaterialBuilder {

	private final Set<MaterialFlag> flags = new HashSet<>();
	private ResourceLocation textureSet = NCMaterialTextureSets.DULL;
	private @Nullable Integer color;

	@Override
	public MaterialBuilder dust() {
		return this.flag(NCMaterialFlags.DUST);
	}

	@Override
	public MaterialBuilder ingot() {
		return this.flag(NCMaterialFlags.INGOT);
	}

	@Override
	public MaterialBuilder gem() {
		return this.flag(NCMaterialFlags.GEM);
	}

	@Override
	public MaterialBuilder color(final int rgb) {
		this.color = ARGB.opaque(rgb);
		return this;
	}

	@Override
	public MaterialBuilder textureSet(final ResourceLocation textureSet) {
		this.textureSet = textureSet;
		return this;
	}

	private MaterialBuilder flag(final MaterialFlag flag) {
		this.flags.add(flag);
		return this;
	}

	public Material build() {
		return new MaterialImpl(this.flags, this.color, this.textureSet);
	}
}
