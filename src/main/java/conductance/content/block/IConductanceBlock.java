package conductance.content.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import conductance.content.item.IConductanceItem;

public interface IConductanceBlock extends IConductanceItem {

	default TagKey<Block> getMiningToolTag() {
		return BlockTags.MINEABLE_WITH_PICKAXE;
	}
}
