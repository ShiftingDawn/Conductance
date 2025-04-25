package conductance.api.plugin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import conductance.api.material.MaterialOreType;

public interface MaterialOreTypeBuilder {

	MaterialOreTypeBuilder blockType(MaterialOreType.OreBlockType blockType);

	MaterialOreTypeBuilder doubleOutput();

	MaterialOreTypeBuilder hasGravity();

	MaterialOreTypeBuilder requiredTool(TagKey<Block> requiredToolType);

	MaterialOreType build();
}
