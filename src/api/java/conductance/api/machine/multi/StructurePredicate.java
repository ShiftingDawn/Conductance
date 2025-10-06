package conductance.api.machine.multi;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface StructurePredicate {

	boolean test(BlockAndTintGetter level, BlockPos pos, BlockState state, StructureCheckContext ctx);

	static StructurePredicate isAny() {
		return (level, pos, state, ctx) -> true;
	}

	static StructurePredicate isAir() {
		return StructurePredicate.isBlock(Blocks.AIR);
	}

	static StructurePredicate isState(final BlockState expected) {
		return (level, pos, state, ctx) -> state == expected;
	}

	static StructurePredicate isState(final Supplier<BlockState> expected) {
		return (level, pos, state, ctx) -> state == expected.get();
	}

	static StructurePredicate isBlock(final Block expected) {
		return (level, pos, state, ctx) -> state.is(expected);
	}

	static StructurePredicate isBlock(final Supplier<Block> expected) {
		return (level, pos, state, ctx) -> state.is(expected.get());
	}

	static StructurePredicate isTag(final TagKey<Block> expected) {
		return (level, pos, state, ctx) -> state.is(expected);
	}
}
