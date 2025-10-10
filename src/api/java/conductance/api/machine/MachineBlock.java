package conductance.api.machine;

import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.phys.BlockHitResult;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationHelper;
import conductance.api.block.BlockRotationType;
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;

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
			if (machine instanceof final IMultiBlockController<?> multiBlockController && !MultiControllerMachineBlockEntity.checkStructure(multiBlockController, true)) {
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
	public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
		if (level.getBlockEntity(pos) instanceof final MachineBlockEntity<?> machine) {
			machine.onPlaced();
			final Direction facing = BlockRotationHelper.getFacing(state);
			for (final MachineCapability capability : machine.getCapabilities().values()) {
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
			if (level.isClientSide) {
				return (level1, blockPos, blockState, be) -> {
					if (be instanceof final MachineBlockEntity<?> machine) {
						machine.onClientTick();
					}
				};
			} else {
				return (level1, blockPos, blockState, be) -> {
					if (be instanceof final MachineBlockEntity<?> machine) {
						machine.handleServerTick();
					}
				};
			}
		}
		return null;
	}
}
