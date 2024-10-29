package conductance.core.apiimpl;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import lombok.Getter;
import conductance.api.material.MaterialOreType;
import conductance.api.registry.RegistryObject;

public final class MaterialOreTypeImpl extends RegistryObject<ResourceLocation> implements MaterialOreType {

	@Getter
	private final OreBlockType oreBlockType;
	@Getter
	private final ResourceLocation bearingBlockModel;
	@Getter
	private final String unlocalizedNameFactory;
	@Getter
	private final String bearingStoneTagName;
	private final boolean hasDoubleOutput;
	private final boolean hasGravity;
	@Getter
	private final MapColor mapColor;
	@Getter
	private final SoundType soundType;

	public MaterialOreTypeImpl(final ResourceLocation registryKey, final OreBlockType oreBlockType, final ResourceLocation bearingBlockModel, final String unlocalizedNameFactory, final String bearingStoneTagName,
			final boolean hasDoubleOutput, final boolean hasGravity, final MapColor mapColor, final SoundType soundType) {
		super(registryKey);
		this.oreBlockType = oreBlockType;
		this.bearingBlockModel = bearingBlockModel;
		this.unlocalizedNameFactory = unlocalizedNameFactory;
		this.bearingStoneTagName = bearingStoneTagName;
		this.hasDoubleOutput = hasDoubleOutput;
		this.hasGravity = hasGravity;
		this.mapColor = mapColor;
		this.soundType = soundType;
	}

	@Override
	public boolean hasDoubleOutput() {
		return this.hasDoubleOutput;
	}

	@Override
	public boolean hasGravity() {
		return this.hasGravity;
	}
}
