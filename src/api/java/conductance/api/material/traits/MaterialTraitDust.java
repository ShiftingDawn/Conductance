package conductance.api.material.traits;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import lombok.Getter;
import lombok.Setter;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

@Getter
public final class MaterialTraitDust implements IMaterialTrait<MaterialTraitDust> {

	@Setter
	private TagKey<Block> requiredToolTag;
	private int burnTime;

	public MaterialTraitDust(final TagKey<Block> requiredToolTag, final int burnTime) {
		this.requiredToolTag = requiredToolTag;
		this.burnTime = burnTime;
	}

	public MaterialTraitDust() {
		this(BlockTags.NEEDS_STONE_TOOL, 0);
	}

	public void setBurnTime(final int burnTime) {
		if (burnTime < 0) {
			throw new IllegalArgumentException("burnTime cannot be negative!");
		}
		this.burnTime = burnTime;
	}

	@Override
	public void verify(final Material material, final MaterialTraitMap traitMap) {
	}
}
