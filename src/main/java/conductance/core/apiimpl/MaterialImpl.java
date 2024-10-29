package conductance.core.apiimpl;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.registry.RegistryObject;

public final class MaterialImpl extends RegistryObject<ResourceLocation> implements Material {

	@Getter
	private final String unlocalizedName;
	@Getter
	private final MaterialDataMapImpl data;
	@Getter
	private final MaterialTraitMapImpl traits;
	private final MaterialFlagMap flags;

	public MaterialImpl(final ResourceLocation registryKey, final MaterialDataMapImpl data, final MaterialTraitMapImpl traits, final MaterialFlagMap flags) {
		super(registryKey);
		this.unlocalizedName = Util.makeDescriptionId("material", registryKey);
		this.data = data;
		this.traits = traits;
		this.flags = flags;
	}

	void verify(final boolean calculateColor) {
		this.traits.verify();
		this.flags.verify(this);
		this.data.verify(calculateColor);
		// this.calcDecompositionType(); TODO
	}

	@Override
	public boolean hasFlag(final MaterialFlag flag) {
		return this.flags.has(flag);
	}
}
