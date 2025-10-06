package conductance.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import conductance.api.block.BlockRotationHelper;
import conductance.api.util.multiblock.MultiBlockStructure;
import conductance.api.util.multiblock.MultiBlockStructureBuilder;
import conductance.api.util.multiblock.StructureHelper;
import conductance.api.util.multiblock.StructurePredicate;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class TestBlock extends Block {

	public TestBlock(final Properties props) {
		super(props);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(FACING));
	}

	@Override
	protected BlockState rotate(final BlockState state, final Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		state = BlockRotationHelper.setFacingOnPlacement(state, context);
		return state;
	}

	public MultiBlockStructure getStructure() {
		return new MultiBlockStructureBuilder('x', this)
			.slice("aba", "bcb", "aba")
			.slice("b b", "axa", "bab")
			.slice("aba", "bab", "aba")
			.key('a', Blocks.COBBLESTONE)
			.key('b', Blocks.DIRT)
			.key('c', Blocks.DIAMOND_BLOCK)
			.build();
	}

	@Override
	protected InteractionResult useItemOn(final ItemStack stack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hitResult) {
		if (level.isClientSide) {
			final Direction facing = state.getValue(FACING);
			final MultiBlockStructure structure = this.getStructure();
			final BlockPos startPos = StructureHelper.getStructureCheckStartPos(pos, structure, facing);
			for (int x = 0; x < structure.expectedStates().length; ++x) {
				final StructurePredicate[][] slicePredicates = structure.expectedStates()[x];
				for (int y = 0; y < slicePredicates.length; ++y) {
					final StructurePredicate[] layerPredicates = slicePredicates[y];
					for (int z = 0; z < layerPredicates.length; ++z) {
						final StructurePredicate predicate = layerPredicates[z];
						final BlockPos currentPos = StructureHelper.getStructureCheckStartEndPos(startPos, facing, x, y, z);
						if (!predicate.test(level, currentPos, level.getBlockState(currentPos))) {
							player.displayClientMessage(Component.literal("[" + currentPos + "]Expect: " + predicate + ", Actual: " + level.getBlockState(currentPos)), false);
						}
					}
				}
			}
			player.displayClientMessage(Component.literal("DONE"), false);
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}
}
