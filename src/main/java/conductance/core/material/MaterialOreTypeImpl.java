package conductance.core.material;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import lombok.Getter;
import conductance.api.material.MaterialOreType;
import conductance.api.registry.RegistryObject;

final class MaterialOreTypeImpl extends RegistryObject<ResourceLocation> implements MaterialOreType {

	@Getter
	private final OreBlockType oreBlockType;
	@Getter
	private final ResourceLocation bearingBlockModel;
	private final boolean hasDoubleOutput;
	private final boolean hasGravity;
	@Getter
	private final MapColor mapColor;
	@Getter
	private final SoundType soundType;
	@Getter
	private final TagKey<Block> requiredToolTypeTag;

	MaterialOreTypeImpl(
			final ResourceLocation registryKey, final OreBlockType oreBlockType, final ResourceLocation bearingBlockModel,
			final boolean hasDoubleOutput, final boolean hasGravity, final MapColor mapColor, final SoundType soundType, final TagKey<Block> requiredToolTypeTag
	) {
		super(registryKey);
		this.oreBlockType = oreBlockType;
		this.bearingBlockModel = bearingBlockModel;
		this.hasDoubleOutput = hasDoubleOutput;
		this.hasGravity = hasGravity;
		this.mapColor = mapColor;
		this.soundType = soundType;
		this.requiredToolTypeTag = requiredToolTypeTag;
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
