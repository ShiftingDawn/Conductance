package conductance.init.block;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import conductance.api.CAPI;
import conductance.api.block.IGeneratedMiningTags;

public final class CreativeTankBlock extends Block implements IGeneratedMiningTags, EntityBlock {

	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

	public CreativeTankBlock(final Properties props) {
		super(props.noOcclusion());
		this.registerDefaultState(this.getStateDefinition().any().setValue(CreativeTankBlock.LOCKED, false));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(CreativeTankBlock.LOCKED));
	}

	@Override
	protected InteractionResult useItemOn(final ItemStack stack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof final CreativeTankBlockEntity blockEntity) {
			if (player.isCrouching()) {
				final boolean shouldLock = !blockEntity.isLocked();
				if (shouldLock && blockEntity.getFluid().isEmpty()) {
					player.displayClientMessage(Component.translatable("tooltip.creative_tank.locking.failure"), true);
					return InteractionResult.SUCCESS_SERVER;
				}
				if (level.isClientSide) {
					if (shouldLock) {
						player.displayClientMessage(Component.translatable("tooltip.creative_tank.locking.locked", blockEntity.getFluid().getHoverName()), true);
					} else {
						player.displayClientMessage(Component.translatable("tooltip.creative_tank.locking.unlocked"), true);
					}
				} else {
					level.setBlockAndUpdate(pos, state.setValue(CreativeTankBlock.LOCKED, shouldLock));
				}
				return InteractionResult.SUCCESS_SERVER;
			} else {
				final FluidStack fluid = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
				if (!fluid.isEmpty()) {
					if (!blockEntity.isLocked() || blockEntity.getFluid().isEmpty() || FluidStack.isSameFluidSameComponents(blockEntity.getFluid(), fluid)) {
						blockEntity.setFluid(fluid.copyWithAmount(CAPI.BUCKET));
					}
					blockEntity.updateLight(level);
					return InteractionResult.SUCCESS_SERVER;
				}
			}
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public boolean hasDynamicLightEmission(final BlockState state) {
		return true;
	}

	@Override
	public int getLightEmission(final BlockState state, final BlockGetter level, final BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof final CreativeTankBlockEntity blockEntity && !blockEntity.getFluid().isEmpty()) {
			return blockEntity.getFluid().getFluidType().getLightLevel(blockEntity.getFluid());
		}
		return super.getLightEmission(state, level, pos);
	}

	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return new CreativeTankBlockEntity(pos, state);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		return List.of(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolLevelTag() {
		return List.of(BlockTags.NEEDS_IRON_TOOL);
	}
}
