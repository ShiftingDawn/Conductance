package conductance.api.machine.multi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import conductance.api.NCMultiBlockPartCapabilities;
import conductance.api.NCRecipeElementTypes;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.IO;

public abstract class StructurePredicate {

	private final @Getter Set<StructurePredicate> children = new HashSet<>();
	private @Getter int minMatches = -1;
	private @Getter int maxMatches = -1;

	public final StructurePredicate min(final int min) {
		this.minMatches = min;
		return this;
	}

	public final StructurePredicate max(final int max) {
		this.maxMatches = max;
		return this;
	}

	public final StructurePredicate exact(final int exact) {
		return this.min(exact).max(exact);
	}

	protected abstract boolean testInternal(BlockAndTintGetter level, BlockPos pos, BlockState state, StructureCheckContext ctx);

	public final boolean test(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
		if (this.testInternal(level, pos, state, ctx)) {
			this.addMatchToContext(ctx);
			return true;
		}
		return false;
	}

	protected final void addMatchToContext(final StructureCheckContext ctx) {
		final int current = ctx.get(StructureCheckContext.MATCH_COUNT).getOrDefault(this, 0);
		ctx.get(StructureCheckContext.MATCH_COUNT).put(this, current + 1);
	}

	protected final void addChildren(final StructurePredicate... childPredicates) {
		this.children.addAll(Arrays.asList(childPredicates));
	}

	public final StructurePredicate or(final StructurePredicate... otherPredicates) {
		final StructurePredicate result = new StructurePredicate() {
			@Override
			protected boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				boolean success = StructurePredicate.this.test(level, pos, state, ctx);
				for (final StructurePredicate other : otherPredicates) {
					if (other.test(level, pos, state, ctx)) {
						success = true;
					}
				}
				return success;
			}
		};
		result.addChildren(otherPredicates);
		return result;
	}

	public final StructurePredicate and(final StructurePredicate... otherPredicates) {
		final StructurePredicate result = new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				if (!StructurePredicate.this.test(level, pos, state, ctx)) {
					return false;
				}
				for (final StructurePredicate other : otherPredicates) {
					if (!other.test(level, pos, state, ctx)) {
						return false;
					}
				}
				return true;
			}
		};
		result.addChildren(otherPredicates);
		return result;
	}

	public static StructurePredicate isAny() {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return true;
			}
		};
	}

	public static StructurePredicate isNone() {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return false;
			}
		};
	}

	public static StructurePredicate isAir() {
		return StructurePredicate.isBlock(Blocks.AIR);
	}

	public static StructurePredicate isState(final BlockState expected) {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return state == expected;
			}
		};
	}

	public static StructurePredicate isState(final Supplier<BlockState> expected) {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return state == expected.get();
			}
		};
	}

	public static StructurePredicate isBlock(final Block expected) {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return state.is(expected);
			}
		};
	}

	public static StructurePredicate isBlock(final Holder<Block> expected) {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return state.is(expected.value());
			}
		};
	}

	public static StructurePredicate isBlock(final Supplier<Block> expected) {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return state.is(expected.get());
			}
		};
	}

	public static StructurePredicate isTag(final TagKey<Block> expected) {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				return state.is(expected);
			}
		};
	}

	public static StructurePredicate isCapability(final MultiBlockPartCapability capability) {
		return new StructurePredicate() {
			@Override
			public boolean testInternal(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final StructureCheckContext ctx) {
				final BlockEntity blockEntity = level.getBlockEntity(pos);
				return blockEntity instanceof final IMultiBlockPart part && part.getPartCapability() == capability;
			}
		};
	}

	public static StructurePredicate autoCapabilities(final MachineRecipeType recipeType) {
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
