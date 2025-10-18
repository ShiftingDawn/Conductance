package conductance.api.machine;

import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCBlockStateProperties;
import conductance.api.block.BlockRotationHelper;
import conductance.api.block.BlockRotationType;
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.block.InteractType;
import conductance.api.machine.api.IEventListener;
import conductance.api.machine.api.IMachineCapabilityHolder;
import conductance.api.machine.api.IPlacerAware;
import conductance.api.machine.api.IWorkable;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.multi.IMultiBlockController;

public class MachineBlock<T extends MachineBlockEntity<T>> extends Block implements EntityBlock, IGeneratedMiningTags {

	private static final ThreadLocal<BlockRotationType> CURRENT_ROTATION_TYPE = new ThreadLocal<>();
	public static final BooleanProperty TICKING = BooleanProperty.create("ticking");
	private final @Getter MachineType<T> machineType;

	public MachineBlock(final BlockBehaviour.Properties properties, final MachineType<T> machineType) {
		super(Util.make(properties, ignored -> MachineBlock.CURRENT_ROTATION_TYPE.set(machineType.getRotationType())));
		this.machineType = machineType;
		this.registerDefaultState(this.createDefaultState());
	}

	protected BlockState createDefaultState() {
		return BlockRotationHelper.addToDefaultState(this.machineType.getRotationType(), this.getStateDefinition().any()).setValue(MachineBlock.TICKING, false);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		BlockRotationHelper.addToBlockStateDefinition(MachineBlock.CURRENT_ROTATION_TYPE.get(), builder);
		builder.add(MachineBlock.TICKING);
	}

	@Override
	public MutableComponent getName() {
		return this.machineType.getName();
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		return List.of(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolLevelTag() {
		return List.of(BlockTags.NEEDS_STONE_TOOL);
	}

	@Override
	public BlockEntity newBlockEntity(final BlockPos blockPos, final BlockState blockState) {
		return this.machineType.getBlockEntityType().get().create(blockPos, blockState);
	}

	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		state = BlockRotationHelper.setFacingOnPlacement(state, context);
		return state;
	}

	@Override
	protected BlockState rotate(final BlockState state, final Rotation rotation) {
		return BlockRotationHelper.applyRotation(state, rotation);
	}

	@Override
	protected BlockState mirror(final BlockState state, final Mirror mirror) {
		return BlockRotationHelper.applyMirror(state, mirror);
	}

	@Override
	protected @Nullable MenuProvider getMenuProvider(final BlockState state, final Level level, final BlockPos pos) {
		final BlockEntity mbe = level.getBlockEntity(pos);
		if (mbe instanceof final MachineBlockEntity<?> machine && machine.getMachineType().getGuiSetup() != null) {
			if (machine instanceof final IMultiBlockController<?> multiBlockController && !multiBlockController.isStructureFormed()) {
				return null;
			}
			return new SimpleMenuProvider(
				(containerId, playerInventory, plr) -> new MachineMenu(machine, containerId, ContainerLevelAccess.create(level, pos), plr.getInventory()),
				this.machineType.getName()
			);
		}
		return null;
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
		if (!player.isCrouching()) {
			final MenuProvider menuProvider = state.getMenuProvider(level, pos);
			if (menuProvider != null) {
				if (!level.isClientSide) {
					player.openMenu(menuProvider, buffer -> buffer.writeBlockPos(pos));
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	protected InteractionResult useItemOn(final ItemStack stack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hitResult) {
		if (InteractType.HAMMER.is(stack) && level.getBlockEntity(pos) instanceof final MachineBlockEntity<?> machine) {
			//TODO replace this with controller mechanism
			machine.setWorking(!machine.isWorking());
			InteractType.HAMMER.playSound(level, player, pos);
			return InteractionResult.SUCCESS_SERVER;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public void onBlockStateChange(final LevelReader level, final BlockPos pos, final BlockState oldState, final BlockState newState) {
		if (level.getBlockEntity(pos) instanceof final IEventListener eventListener) {
			eventListener.onBlockStateChanged(oldState, newState);
		}
	}

	@Override
	protected void neighborChanged(final BlockState state, final Level level, final BlockPos pos, final Block neighborBlock, @Nullable final Orientation orientation, final boolean movedByPiston) {
		if (level.getBlockEntity(pos) instanceof final IEventListener eventListener) {
			eventListener.onNeighborChanged();
		}
	}

	@Override
	public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
		final BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity == null) {
			return;
		}
		if (blockEntity instanceof final IPlacerAware placerAware && placer != null) {
			placerAware.setPlacer(placer.getUUID());
		}
		if (blockEntity instanceof final IWorkable workable) {
			if (state.hasProperty(NCBlockStateProperties.WORKING) && state.getValue(NCBlockStateProperties.WORKING) != workable.getDefaultWorkingState()) {
				level.setBlockAndUpdate(pos, state.setValue(NCBlockStateProperties.WORKING, workable.getDefaultWorkingState()));
			}
		}
		if (blockEntity instanceof final IEventListener eventListener) {
			eventListener.onPlaced();
		}
		if (blockEntity instanceof final IMachineCapabilityHolder capabilityHolder) {
			final Direction facing = BlockRotationHelper.getFacing(state);
			for (final MachineCapability capability : capabilityHolder.getCapabilities().values()) {
				if (capability instanceof final IItemAutoOutput itemAutoOutput) {
					itemAutoOutput.setItemAutoOutputSide(facing.getOpposite());
				}
				if (capability instanceof final IFluidAutoOutput fluidAutoOutput) {
					fluidAutoOutput.setFluidAutoOutputSide(facing.getOpposite());
				}
			}
		}
	}

	@Override
	public @Nullable <BE extends BlockEntity> BlockEntityTicker<BE> getTicker(final Level level, final BlockState state, final BlockEntityType<BE> blockEntityType) {
		if (blockEntityType == this.machineType.getBlockEntityType().get() && state.getValueOrElse(MachineBlock.TICKING, false)) {
			if (!level.isClientSide) {
				return (lvl, blockPos, blockState, be) -> {
					if (be instanceof final BaseBlockEntity baseBlockEntity) {
						baseBlockEntity.handleServerTick();
					}
				};
			} else {
				return (lvl, blockPos, blockState, be) -> {
					if (be instanceof final IEventListener eventListener) {
						eventListener.onClientTick();
					}
				};
			}
		}
		return null;
	}
}
