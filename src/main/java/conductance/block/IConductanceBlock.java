package conductance.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import conductance.item.IConductanceItem;

public interface IConductanceBlock extends IConductanceItem {

	default TagKey<Block> getMiningToolTag() {
		return BlockTags.MINEABLE_WITH_PICKAXE;
	}
}
