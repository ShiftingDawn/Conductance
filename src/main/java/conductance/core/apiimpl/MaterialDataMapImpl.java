package conductance.core.apiimpl;

import java.util.function.ToLongFunction;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import conductance.api.NCTextureSets;
import conductance.api.material.Material;
import conductance.api.material.MaterialDataMap;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.PeriodicElement;

@Getter
public final class MaterialDataMapImpl implements MaterialDataMap {

	private int burnTime;
	private TagKey<Block> blockRequiredToolTag = BlockTags.NEEDS_STONE_TOOL;
	private int blockLightLevel;

	private int color;
	private MaterialTextureSet textureSet;

	@Getter(AccessLevel.PACKAGE)
	private ImmutableList<MaterialStack> components;
	@Nullable
	@Getter(AccessLevel.PACKAGE)
	private PeriodicElement periodicElement;
	private long protons = -1;
	private long neutrons = -1;
	private long mass = -1;

	void verify(final boolean doCalculateColor) {
		if (this.burnTime <= 0) {
			this.burnTime = -1;
		}
		this.blockLightLevel = Mth.clamp(this.blockLightLevel, 0, 15);
		if (this.color == -1) {
			if (!doCalculateColor || this.components.isEmpty()) {
				this.color = 0xFFFFFF;
			} else {
				long calculatedColor = 0;
				int componentCount = 0;
				for (final MaterialStack component : this.components) {
					calculatedColor += component.material().getMaterialColorRGB();
					componentCount += component.count();
				}
				this.color = (int) (calculatedColor / componentCount);
			}
		}
	}

	@Override
	public int getMaterialColorRGB() {
		return this.color;
	}

	@Override
	public int getMaterialColorARGB() {
		return this.getMaterialColorRGB() | 0xFF000000;
	}

	@Override
	public long getProtons() {
		if (this.protons == -1) {
			this.protons = this.calc(PeriodicElement::protons, Material::getProtons, 43);
		}
		return this.protons;
	}

	@Override
	public long getNeutrons() {
		if (this.neutrons == -1) {
			this.neutrons = this.calc(PeriodicElement::neutrons, Material::getNeutrons, 55);
		}
		return this.neutrons;
	}

	@Override
	public long getMass() {
		if (this.mass == -1) {
			this.mass = this.calc(PeriodicElement::mass, Material::getMass, 43);
		}
		return this.mass;
	}

	private long calc(final ToLongFunction<PeriodicElement> rootProvider, final ToLongFunction<Material> provider, final long fallback) {
		if (this.periodicElement != null) {
			return rootProvider.applyAsLong(this.periodicElement);
		} else if (this.components.isEmpty()) {
			return fallback; // Technetium
		} else {
			long total = 0;
			long amount = 0;
			for (final MaterialStack material : this.components) {
				total += material.count() * provider.applyAsLong(material.material());
				amount += material.count();
			}
			return total / amount;
		}
	}

	@Setter
	@Accessors(fluent = true)
	static class Builder {

		private int burnTime = 0;
		private TagKey<Block> requiredToolTag = BlockTags.NEEDS_STONE_TOOL;
		private int lightLevel = 0;
		private int color = -1;
		private MaterialTextureSet textureSet = NCTextureSets.DULL;
		private PeriodicElement periodicElement;

		public MaterialDataMapImpl build(final ImmutableList<MaterialStack> components) {
			return Util.make(new MaterialDataMapImpl(), result -> {
				result.burnTime = this.burnTime;
				result.blockRequiredToolTag = this.requiredToolTag;
				result.blockLightLevel = this.lightLevel;
				result.color = this.color;
				result.textureSet = this.textureSet;
				result.periodicElement = this.periodicElement;
				result.components = components;
			});
		}
	}
}
