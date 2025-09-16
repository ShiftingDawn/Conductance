package conductance.core.material;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialTextureSets;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.event.MaterialBuilder;

final class MaterialBuilderImpl implements MaterialBuilder {

	private final Set<MaterialFlag> flags = new HashSet<>();
	private final Map<MaterialProp<?>, Object> props = new IdentityHashMap<>();
	private ResourceLocation textureSet = NCMaterialTextureSets.DULL;
	private @Nullable Integer color;

	@Override
	public MaterialBuilder flag(final MaterialFlag flag) {
		this.flags.add(flag);
		return this;
	}

	@Override
	public <T> MaterialBuilder prop(final MaterialProp<T> property, final T value) {
		this.props.put(property, value);
		return this;
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

	public Material build() {
		return new MaterialImpl(this.flags, this.props, this.color, this.textureSet);
	}
}
