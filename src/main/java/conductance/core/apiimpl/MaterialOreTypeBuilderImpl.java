package conductance.core.apiimpl;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import conductance.api.CAPI;
import conductance.api.material.MaterialOreType;
import conductance.api.plugin.MaterialOreTypeBuilder;

final class MaterialOreTypeBuilderImpl implements MaterialOreTypeBuilder {

	private final ResourceLocation registryName;
	private final ResourceLocation bearingBlockModel;
	private final MapColor mapColor;
	private final SoundType soundType;
	private MaterialOreType.OreBlockType blockType = MaterialOreType.OreBlockType.DEFAULT;
	private final TagKey<Block> requiredToolType = BlockTags.MINEABLE_WITH_PICKAXE;
	private boolean doubleOutput = false;
	private boolean hasGravity = false;

	MaterialOreTypeBuilderImpl(final ResourceLocation registryName, final ResourceLocation bearingBlockModel, final MapColor mapColor, final SoundType soundType) {
		this.registryName = registryName;
		this.bearingBlockModel = bearingBlockModel;
		this.mapColor = mapColor;
		this.soundType = soundType;
	}

	@Override
	public MaterialOreTypeBuilder blockType(final MaterialOreType.OreBlockType type) {
		this.blockType = type;
		return this;
	}

	@Override
	public MaterialOreTypeBuilder doubleOutput() {
		this.doubleOutput = true;
		return this;
	}

	@Override
	public MaterialOreTypeBuilder hasGravity() {
		this.hasGravity = true;
		return this;
	}

	@Override
	public MaterialOreType build() {
		final MaterialOreType result = new MaterialOreTypeImpl(
				this.registryName, this.blockType, this.bearingBlockModel,
				this.doubleOutput, this.hasGravity, this.mapColor, this.soundType, this.requiredToolType
		);
		CAPI.regs().materialOreTypes().register(result.getRegistryKey(), result);
		return result;
	}
}
