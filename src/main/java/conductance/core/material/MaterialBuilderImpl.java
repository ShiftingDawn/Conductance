package conductance.core.material;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialProps;
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
	public MaterialBuilder liquid(final int temperature, final int density, final int viscosity) {
		this.props.putIfAbsent(NCMaterialProps.DEFAULT_FLUID, NCMaterialProps.FluidType.LIQUID);
		this.flag(NCMaterialFlags.LIQUID);
		return this.prop(NCMaterialProps.LIQUID_TEMPERATURE, temperature)
				.prop(NCMaterialProps.LIQUID_DENSITY, density)
				.prop(NCMaterialProps.LIQUID_VISCOSITY, viscosity);
	}

	@Override
	public MaterialBuilder gas(final int temperature, final int density, final int viscosity) {
		this.props.putIfAbsent(NCMaterialProps.DEFAULT_FLUID, NCMaterialProps.FluidType.GAS);
		this.flag(NCMaterialFlags.GAS);
		return this.prop(NCMaterialProps.GAS_TEMPERATURE, temperature)
				.prop(NCMaterialProps.GAS_DENSITY, density)
				.prop(NCMaterialProps.GAS_VISCOSITY, viscosity);
	}

	@Override
	public MaterialBuilder plasma(final int temperature, final int density, final int viscosity) {
		this.props.putIfAbsent(NCMaterialProps.DEFAULT_FLUID, NCMaterialProps.FluidType.PLASMA);
		this.flag(NCMaterialFlags.PLASMA);
		return this.prop(NCMaterialProps.PLASMA_TEMPERATURE, temperature)
				.prop(NCMaterialProps.PLASMA_DENSITY, density)
				.prop(NCMaterialProps.PLASMA_VISCOSITY, viscosity);
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
