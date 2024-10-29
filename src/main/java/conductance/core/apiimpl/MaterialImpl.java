package conductance.core.apiimpl;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.registry.RegistryObject;

public class MaterialImpl extends RegistryObject<ResourceLocation> implements Material {

	@Getter
	private final String unlocalizedName;
	@Getter
	private final MaterialDataMapImpl data;
	@Getter
	private final MaterialTraitMapImpl traits;
	private final MaterialFlagMap flags;

	public MaterialImpl(ResourceLocation registryKey, MaterialDataMapImpl data, MaterialTraitMapImpl traits, MaterialFlagMap flags) {
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
	public boolean hasFlag(MaterialFlag flag) {
		return this.flags.has(flag);
	}
}
