package conductance.api.machine.gui;

import java.util.Optional;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineType;

public class MachineMenu extends AbstractContainerMenu {

	private final @Getter MachineType<?> machineType;
	private final @Getter ContainerLevelAccess access;

	public MachineMenu(final MachineType<?> machineType, final int containerId, final ContainerLevelAccess access, final Inventory playerInventory) {
		super(MachineGuiHelper.MENU_TYPE.get(), containerId);
		this.machineType = machineType;
		this.access = access;
		Optional.ofNullable(this.getPlayerInventoryPos()).ifPresent(pos -> {
			for (int y = 0; y < 3; ++y) {
				for (int x = 0; x < 9; ++x) {
					this.addSlot(new Slot(playerInventory, x + (y + 1) * 9, pos.x() + x * 18, pos.y() + y * 18));
				}
			}
		});
		Optional.ofNullable(this.getPlayerHotbarPos()).ifPresent(pos -> {
			for (int i = 0; i < 9; ++i) {
				this.addSlot(new Slot(playerInventory, i, pos.x() + i * 18, pos.y()));
			}
		});
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

	protected Size getContainerSize() {
		return MachineGuiHelper.DEFAULT_CONTAINER_SIZE;
	}

	protected @Nullable Coordinate getPlayerInventoryPos() {
		return MachineGuiHelper.DEFAULT_INVENTORY_POS;
	}

	protected @Nullable Coordinate getPlayerHotbarPos() {
		return MachineGuiHelper.DEFAULT_HOTBAR_POS;
	}
}
