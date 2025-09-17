package conductance.core.material;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTextureSets;
import conductance.api.NCMaterialTraits;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.MaterialBuilder;
import conductance.Conductance;

final class MaterialBuilderImpl implements MaterialBuilder {

	@Getter(AccessLevel.PACKAGE)
	private final Set<MaterialFlag> flags = new HashSet<>();
	@Getter(AccessLevel.PACKAGE)
	private final Map<MaterialTraitKey<?>, MaterialTrait<?>> traits = new IdentityHashMap<>();
	@Getter(AccessLevel.PACKAGE)
	private final Map<MaterialProp<?>, Object> props = new IdentityHashMap<>();

	private ResourceLocation textureSet = NCMaterialTextureSets.DULL;
	private @Nullable Integer color;

	@Override
	public MaterialBuilder flag(final MaterialFlag flag) {
		this.flags.add(flag);
		return this;
	}

	@Override
	public <T extends MaterialTrait<T>> MaterialBuilder trait(final MaterialTraitKey<T> key, final T instance) {
		this.traits.put(key, instance);
		return this;
	}

	@Override
	public MaterialBuilder liquid(final Supplier<MaterialTraitFluid.Liquid> factory) {
		this.props.putIfAbsent(NCMaterialProps.DEFAULT_FLUID, NCMaterialTraits.LIQUID);
		this.trait(NCMaterialTraits.LIQUID, factory.get());
		return this;
	}

	@Override
	public MaterialBuilder gas(final Supplier<MaterialTraitFluid.Gas> factory) {
		this.props.putIfAbsent(NCMaterialProps.DEFAULT_FLUID, NCMaterialTraits.GAS);
		this.trait(NCMaterialTraits.GAS, factory.get());
		return this;
	}

	@Override
	public MaterialBuilder plasma(final Supplier<MaterialTraitFluid.Plasma> factory) {
		this.props.putIfAbsent(NCMaterialProps.DEFAULT_FLUID, NCMaterialTraits.PLASMA);
		this.trait(NCMaterialTraits.PLASMA, factory.get());
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

	public Material build(final ResourceLocation registryKey) {
		Conductance.dispatchAll(ModifyMaterialEventImpl.class, new ModifyMaterialEventImpl(registryKey, this));
		return new MaterialImpl(this.flags, this.traits, this.props, this.color, this.textureSet);
	}
}
