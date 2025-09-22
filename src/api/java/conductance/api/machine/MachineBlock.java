package conductance.api.machine;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import lombok.Getter;
import conductance.api.machine.gui.MachineMenu;

public class MachineBlock<T extends MachineBlockEntity<T>> extends Block {

	private final @Getter MachineType<T> machineType;

	public MachineBlock(final BlockBehaviour.Properties properties, final MachineType<T> machineType) {
		super(properties);
		this.machineType = machineType;
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
		//TODO only open gui when machine has a gui
		if (player instanceof final ServerPlayer serverPlayer) {
			serverPlayer.openMenu(new SimpleMenuProvider(
				(containerId, playerInventory, plr) -> new MachineMenu(this.machineType, containerId, ContainerLevelAccess.create(level, pos)),
				Component.translatable(Util.makeDescriptionId("machine", this.machineType.getId()))
			), buffer -> buffer.writeResourceLocation(this.machineType.getId()));
		}
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}
}
