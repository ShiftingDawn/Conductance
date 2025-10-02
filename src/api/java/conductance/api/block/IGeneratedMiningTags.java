package conductance.api.block;

import java.util.List;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface IGeneratedMiningTags {

	List<TagKey<Block>> getRequiredToolTypeTag();

	List<TagKey<Block>> getRequiredToolLevelTag();
}
