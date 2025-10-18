package conductance.init.block;

import java.util.List;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import conductance.api.block.IGeneratedLootTable;
import conductance.api.block.IGeneratedMiningTags;
import conductance.lib.mixin.BlockBehaviourPropertiesAccessor;

public class SimpleBlock extends Block implements IGeneratedLootTable, IGeneratedMiningTags {

	public SimpleBlock(final Properties props) {
		super(SimpleBlock.fixProps(props));
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		return List.of(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolLevelTag() {
		return List.of(BlockTags.NEEDS_STONE_TOOL);
	}

	private static BlockBehaviour.Properties fixProps(final BlockBehaviour.Properties props) {
		final BlockBehaviourPropertiesAccessor accessor = (BlockBehaviourPropertiesAccessor) props;
		final BlockBehaviourPropertiesAccessor reference = (BlockBehaviourPropertiesAccessor) Blocks.IRON_BLOCK.properties();
		if (accessor.getExplosionResistance() == 0) {
			props.explosionResistance(reference.getExplosionResistance());
		}
		if (accessor.getDestroyTime() == 0) {
			props.destroyTime(reference.getDestroyTime());
		}
		return props;
	}
}
