package conductance.api.util.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface StructurePredicate {

	boolean test(BlockAndTintGetter level, BlockPos pos, BlockState state);

	static StructurePredicate isAny() {
		return (level, pos, state) -> true;
	}

	static StructurePredicate isAir() {
		return StructurePredicate.isBlock(Blocks.AIR);
	}

	static StructurePredicate isState(final BlockState expected) {
		return (level, pos, state) -> state == expected;
	}

	static StructurePredicate isBlock(final Block expected) {
		return (level, pos, state) -> state.is(expected);
	}

	static StructurePredicate isTag(final TagKey<Block> expected) {
		return (level, pos, state) -> state.is(expected);
	}
}
