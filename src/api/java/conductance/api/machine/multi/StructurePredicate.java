package conductance.api.machine.multi;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.NCRecipeElementTypes;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;

public interface StructurePredicate {

	boolean test(BlockAndTintGetter level, BlockPos pos, BlockState state, StructureCheckContext ctx);

	default StructurePredicate or(final StructurePredicate... otherPredicates) {
		return (level, pos, state, ctx) -> {
			if (this.test(level, pos, state, ctx)) {
				return true;
			}
			for (final StructurePredicate other : otherPredicates) {
				if (other.test(level, pos, state, ctx)) {
					return true;
				}
			}
			return false;
		};
	}

	default StructurePredicate and(final StructurePredicate... otherPredicates) {
		return (level, pos, state, ctx) -> {
			if (!this.test(level, pos, state, ctx)) {
				return false;
			}
			for (final StructurePredicate other : otherPredicates) {
				if (!other.test(level, pos, state, ctx)) {
					return false;
				}
			}
			return true;
		};
	}

	static StructurePredicate isAny() {
		return (level, pos, state, ctx) -> true;
	}

	static StructurePredicate isNone() {
		return (level, pos, state, ctx) -> false;
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

	static StructurePredicate isBlock(final Holder<Block> expected) {
		return (level, pos, state, ctx) -> state.is(expected.value());
	}

	static StructurePredicate isBlock(final Supplier<Block> expected) {
		return (level, pos, state, ctx) -> state.is(expected.get());
	}

	static StructurePredicate isTag(final TagKey<Block> expected) {
		return (level, pos, state, ctx) -> state.is(expected);
	}

	static StructurePredicate isCapability(final MultiBlockPartCapability capability) {
		return (level, pos, state, ctx) ->
			level.getBlockEntity(pos) instanceof final IMultiBlockPart part && part.getPartCapability() == capability;
	}

	static StructurePredicate autoCapabilities(final MachineRecipeType recipeType) {
		final boolean inItems = recipeType.getLimit(IO.IN, NCRecipeElementTypes.ITEM) > 0;
		final boolean inFluids = recipeType.getLimit(IO.IN, NCRecipeElementTypes.FLUID) > 0;
		final boolean outItems = recipeType.getLimit(IO.OUT, NCRecipeElementTypes.ITEM) > 0;
		final boolean outFluids = recipeType.getLimit(IO.OUT, NCRecipeElementTypes.FLUID) > 0;
		StructurePredicate predicate = StructurePredicate.isNone();
		if (inItems) {
			predicate = predicate.or(StructurePredicate.isCapability(NCMultiBlockPartCapabilities.ITEMS_IN));
		}
		if (outItems) {
			predicate = predicate.or(StructurePredicate.isCapability(NCMultiBlockPartCapabilities.ITEMS_OUT));
		}
		if (inFluids) {
			predicate = predicate.or(StructurePredicate.isCapability(NCMultiBlockPartCapabilities.FLUIDS_IN));
		}
		if (outFluids) {
			predicate = predicate.or(StructurePredicate.isCapability(NCMultiBlockPartCapabilities.FLUIDS_OUT));
		}
		return predicate;
	}
}
