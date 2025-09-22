package conductance.api.machine.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import lombok.Getter;
import conductance.api.machine.MachineGuiHelper;
import conductance.api.machine.MachineType;

public class MachineMenu extends AbstractContainerMenu {

	private final @Getter MachineType<?> machineType;
	private final @Getter ContainerLevelAccess access;

	public MachineMenu(final MachineType<?> machineType, final int containerId, final ContainerLevelAccess access) {
		super(MachineGuiHelper.MENU_TYPE.get(), containerId);
		this.machineType = machineType;
		this.access = access;
	}

	@Override
	public ItemStack quickMoveStack(final Player player, final int i) {
		//TODO implement
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(final Player player) {
		return AbstractContainerMenu.stillValid(this.access, player, this.machineType.getBlock().get());
	}

	protected boolean hasPlayerInventory() {
		return true;
	}

	protected boolean hasPlayerHotbar() {
		return true;
	}
}
