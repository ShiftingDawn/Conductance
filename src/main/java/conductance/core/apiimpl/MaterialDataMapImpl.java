package conductance.core.apiimpl;

import java.util.function.ToLongFunction;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import conductance.api.NCTextureSets;
import conductance.api.material.Material;
import conductance.api.material.MaterialDataMap;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.PeriodicElement;

@Builder(access = AccessLevel.PACKAGE)
@Getter
public class MaterialDataMapImpl implements MaterialDataMap {

	@Builder.Default
	private int color = -1;
	@Nullable
	PeriodicElement periodicElement;
	@Builder.Default
	MaterialTextureSet textureSet = NCTextureSets.DULL;
	ImmutableList<MaterialStack> components;
	@Builder.Default
	private long protons = -1;
	@Builder.Default
	private long neutrons = -1;
	@Builder.Default
	private long mass = -1;

	void verify(final boolean doCalculateColor) {
		if (this.color == -1) {
			if (!doCalculateColor || this.components.isEmpty()) {
				this.color = 0xFFFFFF;
			} else {
				long color = 0;
				int componentCount = 0;
				for (final MaterialStack component : this.components) {
					color += component.material().getMaterialColorRGB();
					componentCount += component.count();
				}
				this.color = (int) (color / componentCount);
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

	public long getProtons() {
		if (this.protons == -1) {
			this.protons = this.calc(PeriodicElement::protons, Material::getProtons, 43);
		}
		return this.protons;
	}

	public long getNeutrons() {
		if (this.neutrons == -1) {
			this.neutrons = this.calc(PeriodicElement::neutrons, Material::getNeutrons, 55);
		}
		return this.neutrons;
	}

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
			long total = 0, amount = 0;
			for (final MaterialStack material : this.components) {
				total += material.count() * provider.applyAsLong(material.material());
				amount += material.count();
			}
			return total / amount;
		}
	}
}
