package conductance.core.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureSets;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.material.traits.MaterialTraitDust;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.material.traits.MaterialTraitGem;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.material.traits.MaterialTraitOre;
import conductance.api.material.traits.MaterialTraitWire;
import conductance.api.material.traits.MaterialTraitWood;
import conductance.api.plugin.MaterialBuilder;
import conductance.api.util.tier.Tier;

final class MaterialBuilderImpl implements MaterialBuilder {

	private final Map<MaterialTraitKey<?>, IMaterialTrait<?>> traits = new ConcurrentHashMap<>();
	private final Set<MaterialFlag> flags = new HashSet<>();
	private final ResourceLocation registryName;
	private final List<MaterialStack> componentList = new ArrayList<>();
	private ResourceLocation textureSet = NCTextureSets.DULL;
	@Nullable
	private PeriodicElement periodicElement;
	@Nullable
	private MaterialTraitKey<? extends MaterialTraitFluid<?>> defaultFluid;
	private TagKey<Block> requiredTool = BlockTags.NEEDS_STONE_TOOL;
	private int burnTime = 0;
	private int lightLevel = 0;
	private int color = -1;
	private boolean calculateColor = false;

	public MaterialBuilderImpl(final ResourceLocation registryName) {
		this.registryName = registryName;
	}

	private <T extends IMaterialTrait<T>> MaterialBuilder set(final MaterialTraitKey<T> trait, final T value) {
		this.traits.put(trait, value);
		return this;
	}

	@Override
	public MaterialBuilder dust() {
		return this.set(NCMaterialTraits.DUST, new MaterialTraitDust());
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
		return this.set(NCMaterialTraits.INGOT, new MaterialTraitIngot(null, null));
	}

	@Override
	public MaterialBuilder ingot(final TagKey<Block> requiredToolTag) {
		this.dust(requiredToolTag);
		return this.set(NCMaterialTraits.INGOT, new MaterialTraitIngot(null, null));
	}

	@Override
	public MaterialBuilder ingot(final TagKey<Block> requiredToolTag, final int burnTime) {
		this.dust(requiredToolTag, burnTime);
		return this.set(NCMaterialTraits.INGOT, new MaterialTraitIngot(null, null));
	}

	@Override
	public MaterialBuilder ingot(final Supplier<MaterialTraitIngot> factory) {
		this.dust();
		return this.set(NCMaterialTraits.INGOT, factory.get());
	}

	@Override
	public MaterialBuilder ingot(final TagKey<Block> requiredToolTag, final Supplier<MaterialTraitIngot> factory) {
		this.dust(requiredToolTag);
		return this.set(NCMaterialTraits.INGOT, factory.get());
	}

	@Override
	public MaterialBuilder ingot(final TagKey<Block> requiredToolTag, final int burnTime, final Supplier<MaterialTraitIngot> factory) {
		this.dust(requiredToolTag, burnTime);
		return this.set(NCMaterialTraits.INGOT, factory.get());
	}

	@Override
	public MaterialBuilder gem() {
		this.dust();
		return this.set(NCMaterialTraits.GEM, new MaterialTraitGem());
	}

	@Override
	public MaterialBuilder gem(final TagKey<Block> requiredToolTag) {
		this.dust(requiredToolTag);
		return this.set(NCMaterialTraits.GEM, new MaterialTraitGem());

	}

	@Override
	public MaterialBuilder gem(final TagKey<Block> requiredToolTag, final int burnTime) {
		this.dust(requiredToolTag, burnTime);
		return this.set(NCMaterialTraits.GEM, new MaterialTraitGem());
	}

	@Override
	public MaterialBuilder liquid(final Supplier<MaterialTraitFluid.Liquid> factory) {
		this.set(NCMaterialTraits.LIQUID, factory.get());
		this.defaultFluid(NCMaterialTraits.LIQUID, false);
		return this;
	}

	@Override
	public MaterialBuilder liquid() {
		return this.liquid(() -> new MaterialTraitFluid.Liquid(-1, -1, -1));
	}

	@Override
	public MaterialBuilder liquid(final int temperature) {
		return this.liquid(() -> new MaterialTraitFluid.Liquid(-1, temperature, -1));
	}

	@Override
	public MaterialBuilder gas(final Supplier<MaterialTraitFluid.Gas> factory) {
		this.set(NCMaterialTraits.GAS, factory.get());
		this.defaultFluid(NCMaterialTraits.GAS, false);
		return this;
	}

	@Override
	public MaterialBuilder gas() {
		return this.gas(() -> new MaterialTraitFluid.Gas(-1, -1, -1));
	}

	@Override
	public MaterialBuilder gas(final int temperature) {
		return this.gas(() -> new MaterialTraitFluid.Gas(-1, temperature, -1));
	}

	@Override
	public MaterialBuilder plasma(final Supplier<MaterialTraitFluid.Plasma> factory) {
		this.set(NCMaterialTraits.PLASMA, factory.get());
		this.defaultFluid(NCMaterialTraits.PLASMA, false);
		return this;
	}

	@Override
	public MaterialBuilder plasma() {
		return this.plasma(() -> new MaterialTraitFluid.Plasma(-1, -1, -1));
	}

	@Override
	public MaterialBuilder plasma(final int temperature) {
		return this.plasma(() -> new MaterialTraitFluid.Plasma(-1, temperature, -1));
	}

	@Override
	public MaterialBuilder defaultFluid(final MaterialTraitKey<? extends MaterialTraitFluid<?>> fluidType) {
		return this.defaultFluid(fluidType, true);
	}

	private MaterialBuilder defaultFluid(final MaterialTraitKey<? extends MaterialTraitFluid<?>> fluidType, final boolean override) {
		if (this.defaultFluid == null || override) {
			this.defaultFluid = fluidType;
		}
		return this;
	}

	@Override
	public MaterialBuilder requiredTool(final TagKey<Block> requiredToolTag) {
		this.requiredTool = Objects.requireNonNull(requiredToolTag);
		return this;
	}

	@Override
	public MaterialBuilder burnTime(final int time) {
		this.burnTime = time;
		return this;
	}

	@Override
	public MaterialBuilder lightLevel(final int level) {
		this.lightLevel = level;
		return this;
	}

	@Override
	public MaterialBuilder color(final int clr) {
		this.color = clr;
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
	public MaterialBuilder textureSet(final ResourceLocation set) {
		this.textureSet = Objects.requireNonNull(set);
		return this;
	}

	@Override
	public MaterialBuilder formula(final String formula) {
		// TODO formula
		return this;
	}

	@Override
	public MaterialBuilder components(final Object... components) {
		for (int i = 0; i < components.length; ++i) {
			final Material material = components[i] instanceof final CharSequence str ? CAPI.regs().materials().get(ResourceLocation.parse(str.toString())) : (Material) components[i];
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
	public MaterialBuilder flags(final Collection<MaterialFlag> preset, final MaterialFlag... flagsToAdd) {
		this.flags.addAll(preset);
		this.flags.addAll(Arrays.asList(flagsToAdd));
		return this;
	}

	@Override
	public MaterialBuilder flags(final MaterialFlag... flagsToAdd) {
		this.flags.addAll(Arrays.asList(flagsToAdd));
		return this;
	}

	@Override
	public MaterialBuilder periodicElement(final PeriodicElement element) {
		this.periodicElement = element;
		return this;
	}

	@Override
	public MaterialBuilder ore(final int dropMultiplier, final int byproductMultiplier, final boolean emissive, @Nullable final Supplier<Material> smeltResult, @Nullable final Supplier<Material> pulverizeResult) {
		if (dropMultiplier <= 0) {
			throw new IllegalArgumentException("dropMultiplier cannot be <= 0!");
		}
		if (byproductMultiplier <= 0) {
			throw new IllegalArgumentException("byproductMultiplier cannot be <= 0!");
		}
		return this.set(NCMaterialTraits.ORE, new MaterialTraitOre(dropMultiplier, byproductMultiplier, emissive, smeltResult, pulverizeResult));
	}

	@Override
	public MaterialBuilder wood() {
		return this.set(NCMaterialTraits.WOOD, new MaterialTraitWood());
	}

	@Override
	public MaterialBuilder wire(final Tier tier, final int amperage) {
		this.dust();
		return this.set(NCMaterialTraits.WIRE, new MaterialTraitWire(tier, amperage));
	}

	public Material build() {
		return Util.make(new MaterialImpl(this.registryName), result -> {
			result.getTraits().putAll(this.traits);
			result.getFlags().addAll(this.flags);
			result.setPeriodicElement(this.periodicElement);
			this.componentList.forEach(stack -> result.getComponents().put(stack.getMaterial(), stack.getCount()));
			result.setTextureSet(this.textureSet);
			result.setColor(this.calculateColor ? null : this.color);
			result.setDefaultFluid(this.defaultFluid);
			result.setBlockRequiredToolTag(this.requiredTool);
			result.setBurnTime(this.burnTime);
			result.setBlockLightLevel(this.lightLevel);
		});
	}
}
