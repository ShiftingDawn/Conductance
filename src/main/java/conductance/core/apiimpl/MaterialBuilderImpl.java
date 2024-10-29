package conductance.core.apiimpl;

import java.util.*;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableList;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.api.material.*;
import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.plugin.MaterialBuilder;

public final class MaterialBuilderImpl implements MaterialBuilder {

	private final ResourceLocation registryName;
	private final MaterialDataMapImpl.MaterialDataMapImplBuilder data;
	private final MaterialTraitMapImpl traits;
	private final MaterialFlagMap flags;
	private final List<MaterialStack> componentList = new ArrayList<>();
	private boolean calculateColor = false;

	public MaterialBuilderImpl(final ResourceLocation registryName) {
		this.registryName = registryName;
		this.data = new MaterialDataMapImpl.MaterialDataMapImplBuilder();
		this.traits = new MaterialTraitMapImpl();
		this.flags = new MaterialFlagMap();
	}

	@Override
	public MaterialBuilder dust() {
		this.traits.set(NCMaterialTraits.DUST, new MaterialTraitDust());
		return this;
	}

	@Override
	public MaterialBuilder dust(final TagKey<Block> requiredToolTag) {
		return this.dust(requiredToolTag, 0);
	}

	@Override
	public MaterialBuilder dust(final TagKey<Block> requiredToolTag, final int burnTime) {
		this.dust();
		this.requiredTool(requiredToolTag);
		this.burnTime(burnTime);
		return this;
	}

	@Override
	public MaterialBuilder ingot() {
		this.dust();
		this.traits.set(NCMaterialTraits.INGOT, new MaterialTraitIngot());
		return this;
	}

	@Override
	public MaterialBuilder ingot(final TagKey<Block> requiredToolTag) {
		this.dust(requiredToolTag);
		this.traits.set(NCMaterialTraits.INGOT, new MaterialTraitIngot());
		return this;
	}

	@Override
	public MaterialBuilder ingot(final TagKey<Block> requiredToolTag, final int burnTime) {
		this.dust(requiredToolTag, burnTime);
		this.traits.set(NCMaterialTraits.INGOT, new MaterialTraitIngot());
		return this;
	}

	@Override
	public MaterialBuilder gem() {
		this.dust();
		this.traits.set(NCMaterialTraits.GEM, new MaterialTraitGem());
		return this;
	}

	@Override
	public MaterialBuilder gem(final TagKey<Block> requiredToolTag) {
		this.dust(requiredToolTag);
		this.traits.set(NCMaterialTraits.GEM, new MaterialTraitGem());
		return this;
	}

	@Override
	public MaterialBuilder gem(final TagKey<Block> requiredToolTag, final int burnTime) {
		this.dust(requiredToolTag, burnTime);
		this.traits.set(NCMaterialTraits.GEM, new MaterialTraitGem());
		return this;
	}

	@Override
	public MaterialBuilder liquid() {
		this.traits.set(NCMaterialTraits.LIQUID, new MaterialTraitFluid.Liquid());
		return this;
	}

	@Override
	public MaterialBuilder liquid(final int temperature) {
		return this.liquid(b -> b.setTemperature(temperature));
	}

	@Override
	public MaterialBuilder liquid(final Consumer<MaterialTraitFluid.Liquid> builder) {
		this.traits.set(NCMaterialTraits.LIQUID, Util.make(new MaterialTraitFluid.Liquid(), Objects.requireNonNull(builder)));
		return this;
	}

	@Override
	public MaterialBuilder gas() {
		this.traits.set(NCMaterialTraits.GAS, new MaterialTraitFluid.Gas());
		return this;
	}

	@Override
	public MaterialBuilder gas(final int temperature) {
		return this.gas(b -> b.setTemperature(temperature));
	}

	@Override
	public MaterialBuilder gas(final Consumer<MaterialTraitFluid.Gas> builder) {
		this.traits.set(NCMaterialTraits.GAS, Util.make(new MaterialTraitFluid.Gas(), Objects.requireNonNull(builder)));
		return this;
	}

	@Override
	public MaterialBuilder plasma() {
		this.traits.set(NCMaterialTraits.PLASMA, new MaterialTraitFluid.Plasma());
		return this;
	}

	@Override
	public MaterialBuilder plasma(final int temperature) {
		return this.plasma(b -> b.setTemperature(temperature));
	}

	@Override
	public MaterialBuilder plasma(final Consumer<MaterialTraitFluid.Plasma> builder) {
		this.traits.set(NCMaterialTraits.PLASMA, Util.make(new MaterialTraitFluid.Plasma(), Objects.requireNonNull(builder)));
		return this;
	}

	@Override
	public MaterialBuilder requiredTool(final TagKey<Block> requiredToolTag) {
		this.data.blockRequiredToolTag(Objects.requireNonNull(requiredToolTag));
		return this;
	}

	@Override
	public MaterialBuilder burnTime(final int burnTime) {
		this.data.burnTime(burnTime);
		return this;
	}

	@Override
	public MaterialBuilder lightLevel(final int lightLevel) {
		this.data.blockLightLevel(lightLevel);
		return this;
	}

	@Override
	public MaterialBuilder color(final int color) {
		this.data.color(color);
		return this;
	}

	@Override
	public MaterialBuilder color(final int r, final int g, final int b) {
		return this.color(((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff));
	}

	@Override
	public MaterialBuilder calcColor() {
		this.calculateColor = true;
		return this;
	}

	@Override
	public MaterialBuilder textureSet(final MaterialTextureSet set) {
		this.data.textureSet(Objects.requireNonNull(set));
		return this;
	}

	@Override
	public MaterialBuilder formula(final String formula) {
		// TODO
		return this;
	}

	@Override
	public MaterialBuilder components(final Object... components) {
		for (int i = 0; i < components.length; ++i) {
			final Material material = components[i] instanceof final CharSequence str ? CAPI.REGS.materials().get(ResourceLocation.parse(str.toString())) : (Material) components[i];
			long count = 1;
			if (i < components.length - 1 && components[i + 1] instanceof final Number num) {
				count = num.longValue();
				++i;
			}
			if (material != null) {
				this.componentList.add(new MaterialStack(material, count));
			}
		}
		return this;
	}

	@Override
	public MaterialBuilder components(final MaterialStack... components) {
		this.componentList.addAll(Arrays.asList(components));
		return this;
	}

	@Override
	public MaterialBuilder components(final List<MaterialStack> components) {
		this.componentList.addAll(components);
		return this;
	}

	@Override
	public MaterialBuilder flags(final MaterialFlag... flags) {
		this.flags.add(flags);
		return this;
	}

	@Override
	public MaterialBuilder addFlagAndPreset(final Collection<MaterialFlag> preset, final MaterialFlag... flags) {
		this.flags.add(preset.toArray(MaterialFlag[]::new));
		this.flags.add(flags);
		return this;
	}

	@Override
	public MaterialBuilder periodicElement(final PeriodicElement periodicElement) {
		this.data.periodicElement(periodicElement);
		return this;
	}

	@Override
	public MaterialBuilder ore() {
		this.dust();
		//TODO ore
		return this;
	}

	@Override
	public MaterialBuilder ore(final boolean emissive) {
		return this;
	}

	@Override
	public MaterialBuilder ore(final int oreMultiplier, final int byproductMultiplier) {
		return this;
	}

	@Override
	public MaterialBuilder ore(final int oreMultiplier, final int byproductMultiplier, final boolean emissive) {
		return this;
	}

	@Override
	public MaterialBuilder wood() {
		return this;
	}

	@Override
	public Material build() {
		final MaterialDataMapImpl data = this.data.components(ImmutableList.copyOf(this.componentList)).build();
		final MaterialImpl material = new MaterialImpl(this.registryName, data, this.traits, this.flags);
		this.traits.material = material;
		// this.fluidMap.material = material;
		material.verify(this.calculateColor);
		CAPI.REGS.materials().register(material.getRegistryKey(), material);
		return material;
	}
}
