package conductance.core.material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialProps;
import conductance.api.NCMaterialTextureSets;
import conductance.api.NCMaterialTraits;
import conductance.api.NCMaterials;
import conductance.api.material.Material;
import conductance.api.material.MaterialColor;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.event.MaterialBuilder;
import conductance.api.periodicelement.PeriodicElement;
import conductance.Conductance;

@RequiredArgsConstructor
final class MaterialBuilderImpl implements MaterialBuilder {

	@Getter(AccessLevel.PACKAGE)
	private final Set<MaterialFlag> flags = new HashSet<>();
	@Getter(AccessLevel.PACKAGE)
	private final Map<MaterialTraitKey<?>, MaterialTrait<?>> traits = new IdentityHashMap<>();
	@Getter(AccessLevel.PACKAGE)
	private final Map<MaterialProp<?>, Object> props = new IdentityHashMap<>();
	@Getter(AccessLevel.PACKAGE)
	private final Object2IntMap<Material> components = new Object2IntArrayMap<>();

	private final @Nullable PeriodicElement periodicElement;
	private ResourceLocation textureSet = NCMaterialTextureSets.DULL;
	private @Nullable MaterialColor color;
	private @Nullable String chemicalFormula;
	private @Nullable Long protons;
	private @Nullable Long neutrons;
	private @Nullable Long mass;

	@Override
	public MaterialBuilder flag(final MaterialFlag flag) {
		this.flags.add(flag);
		return this;
	}

	@Override
	public MaterialBuilder removeFlag(final MaterialFlag... flagsToRemove) {
		for (final MaterialFlag flag : flagsToRemove) {
			this.flags.remove(flag);
		}
		return this;
	}

	@Override
	public <T extends MaterialTrait<T>> MaterialBuilder trait(final MaterialTraitKey<T> key, final T instance) {
		this.traits.put(key, instance);
		return this;
	}

	@Override
	public <T extends MaterialTrait<T>> MaterialBuilder removeTrait(final MaterialTraitKey<T> traitToRemove) {
		this.traits.remove(traitToRemove);
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
	public <T> MaterialBuilder removeProp(final MaterialProp<T> propToRemove) {
		this.props.remove(propToRemove);
		return this;
	}

	@Override
	public MaterialBuilder color(final int[] colors, final int frametime) {
		this.color = new MaterialColor(colors, frametime);
		return this;
	}

	@Override
	public MaterialBuilder textureSet(final ResourceLocation textureSet) {
		this.textureSet = textureSet;
		return this;
	}

	@Override
	public MaterialBuilder components(final Object... components) {
		for (int i = 0; i < components.length; ++i) {
			final Material material = components[i] instanceof final CharSequence str
				? CAPI.regs().materials().getOptional(ResourceLocation.parse(str.toString())).orElse(NCMaterials.AIR)
				: (Material) components[i];
			int count = 1;
			if (i < components.length - 1 && components[i + 1] instanceof final Number num) {
				count = num.intValue();
				++i;
			}
			if (material != null) {
				if (this.components.containsKey(material)) {
					this.components.put(material, this.components.getInt(material) + count);
				} else {
					this.components.put(material, count);
				}
			}
		}
		return this;
	}

	@Override
	public MaterialBuilder chemicalFormula(final String formula) {
		this.chemicalFormula = formula;
		return this;
	}

	@Override
	public MaterialBuilder protons(final long protons) {
		this.protons = protons;
		return this;
	}

	@Override
	public MaterialBuilder neutrons(final long neutrons) {
		this.neutrons = neutrons;
		return this;
	}

	@Override
	public MaterialBuilder mass(final long mass) {
		this.mass = mass;
		return this;
	}

	public Material build(final ResourceLocation registryKey) {
		Conductance.dispatchAll(ModifyMaterialEventImpl.class, new ModifyMaterialEventImpl(registryKey, this));
		final List<MaterialStack> componentList = Util.make(new ArrayList<>(), list -> this.components.forEach((mat, count) -> list.add(new MaterialStack(mat, count))));
		return new MaterialImpl(this.periodicElement, this.flags, this.traits, this.props, this.color, this.textureSet, componentList, this.chemicalFormula, this.protons, this.neutrons, this.mass);
	}
}
