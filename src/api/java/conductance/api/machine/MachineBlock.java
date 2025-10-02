package conductance.api.machine;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.machine.gui.MachineMenu;

public class MachineBlock<T extends MachineBlockEntity<T>> extends Block implements EntityBlock, IGeneratedMiningTags {

	public static final BooleanProperty TICKING = BooleanProperty.create("ticking");
	private final @Getter MachineType<T> machineType;

	public MachineBlock(final BlockBehaviour.Properties properties, final MachineType<T> machineType) {
		super(properties);
		this.machineType = machineType;
		this.registerDefaultState(this.getStateDefinition().any().setValue(MachineBlock.TICKING, false));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(MachineBlock.TICKING));
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
	protected @Nullable MenuProvider getMenuProvider(final BlockState state, final Level level, final BlockPos pos) {
		final BlockEntity mbe = level.getBlockEntity(pos);
		if (mbe instanceof final MachineBlockEntity<?> machine) {
			return new SimpleMenuProvider(
				(containerId, playerInventory, plr) -> new MachineMenu(machine, containerId, ContainerLevelAccess.create(level, pos), plr.getInventory()),
				this.machineType.getName()
			);
		}
		return null;
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
		//TODO only open gui when machine has a gui
		if (!player.isCrouching()) {
			if (player instanceof final ServerPlayer serverPlayer) {
				serverPlayer.openMenu(state.getMenuProvider(level, pos), buffer -> buffer.writeBlockPos(pos));
			}
			return InteractionResult.SUCCESS;
		}
		return super.useWithoutItem(state, level, pos, player, hitResult);
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
