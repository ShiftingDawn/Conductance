package conductance.api.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineMenu;

public class MachineBlock<T extends MachineBlockEntity<T>> extends Block implements EntityBlock {

	private final @Getter MachineType<T> machineType;

	public MachineBlock(final BlockBehaviour.Properties properties, final MachineType<T> machineType) {
		super(properties);
		this.machineType = machineType;
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
}
