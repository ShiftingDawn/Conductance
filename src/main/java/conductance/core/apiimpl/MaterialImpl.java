package conductance.core.apiimpl;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.registry.RegistryObject;

public final class MaterialImpl extends RegistryObject<ResourceLocation> implements Material {

	@Getter
	private final String unlocalizedName;
	@Getter
	private final MaterialDataMapImpl data;
	@Getter
	private final MaterialTraitMapImpl traits;
	private final MaterialFlagMap flags;
	@Getter
	@Nullable
	private final MaterialTraitKey<? extends MaterialTraitFluid<?>> defaultFluid;

	public MaterialImpl(
			final ResourceLocation registryKey, final MaterialDataMapImpl data, final MaterialTraitMapImpl traits, final MaterialFlagMap flags,
			@Nullable final MaterialTraitKey<? extends MaterialTraitFluid<?>> defaultFluid
	) {
		super(registryKey);
		this.unlocalizedName = Util.makeDescriptionId("material", registryKey);
		this.data = data;
		this.traits = traits;
		this.flags = flags;
		this.defaultFluid = defaultFluid;
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
