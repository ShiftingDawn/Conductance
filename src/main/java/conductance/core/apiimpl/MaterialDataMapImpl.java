package conductance.core.apiimpl;

import java.util.function.ToLongFunction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCTextureSets;
import conductance.api.material.Material;
import conductance.api.material.MaterialDataMap;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.PeriodicElement;

@Getter
final class MaterialDataMapImpl implements MaterialDataMap {

	private int burnTime;
	private TagKey<Block> blockRequiredToolTag = BlockTags.NEEDS_STONE_TOOL;
	private int blockLightLevel;

	private int color;
	private final MaterialTextureSet textureSet;

	@Getter(AccessLevel.PACKAGE)
	private final ImmutableList<MaterialStack> components;
	@Getter(AccessLevel.PACKAGE)
	@Nullable
	private final PeriodicElement periodicElement;
	private long protons = -1;
	private long neutrons = -1;
	private long mass = -1;

	private MaterialDataMapImpl(final Builder builder, final ImmutableList<MaterialStack> components) {
		this.burnTime = builder.burnTime;
		this.blockRequiredToolTag = builder.requiredToolTag;
		this.blockLightLevel = builder.lightLevel;
		this.color = builder.color;
		this.textureSet = builder.textureSet;
		this.components = components;
		this.periodicElement = builder.periodicElement;
	}

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
					calculatedColor += component.getMaterial().getMaterialColorRGB();
					componentCount += (int) component.getCount();
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
				total += material.getCount() * provider.applyAsLong(material.getMaterial());
				amount += material.getCount();
			}
			return total / amount;
		}
	}

	static class Builder {

		private int burnTime = 0;
		private TagKey<Block> requiredToolTag = BlockTags.NEEDS_STONE_TOOL;
		private int lightLevel = 0;
		private int color = -1;
		private MaterialTextureSet textureSet = NCTextureSets.DULL;
		@Nullable
		private PeriodicElement periodicElement;

		public Builder setBurnTime(final int burnTime) {
			this.burnTime = burnTime;
			return this;
		}

		public Builder setRequiredToolTag(final TagKey<Block> requiredToolTag) {
			this.requiredToolTag = requiredToolTag;
			return this;
		}

		public Builder setLightLevel(final int lightLevel) {
			this.lightLevel = lightLevel;
			return this;
		}

		public Builder setColor(final int color) {
			this.color = color;
			return this;
		}

		public Builder setTextureSet(final MaterialTextureSet textureSet) {
			this.textureSet = textureSet;
			return this;
		}

		public Builder setPeriodicElement(final PeriodicElement periodicElement) {
			this.periodicElement = periodicElement;
			return this;
		}

		public MaterialDataMapImpl build(final ImmutableList<MaterialStack> components) {
			return new MaterialDataMapImpl(this, components);
		}
	}
}
