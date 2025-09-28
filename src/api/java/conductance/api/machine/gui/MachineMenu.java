package conductance.api.machine.gui;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import lombok.Getter;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.util.Lazy;

public class MachineMenu extends AbstractContainerMenu {

	public static final Supplier<MenuType<?>> MENU_TYPE = Lazy.of(() -> BuiltInRegistries.MENU.getValue(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "machine")));

	private final @Getter MachineBlockEntity<?> machine;
	private final @Getter ContainerLevelAccess access;
	private final @Getter GuiSetup guiSetup;

	public MachineMenu(final MachineBlockEntity<?> machine, final int containerId, final ContainerLevelAccess access, final Inventory playerInventory) {
		super(MachineMenu.MENU_TYPE.get(), containerId);
		this.machine = machine;
		this.access = access;
		this.guiSetup = Objects.requireNonNull(machine.getMachineType().getGuiSetup(), "Opened a menu for a machine without a GuiSetup!");
		this.guiSetup.addSlots(this, this::addSlot);
		Optional.ofNullable(this.guiSetup.getPlayerInventoryPos()).ifPresent(pos -> {
			for (int y = 0; y < 3; ++y) {
				for (int x = 0; x < 9; ++x) {
					this.addSlot(new Slot(playerInventory, x + (y + 1) * 9, pos.x() + x * 18, pos.y() + y * 18));
				}
			}
		});
		Optional.ofNullable(this.guiSetup.getPlayerHotbarPos()).ifPresent(pos -> {
			for (int i = 0; i < 9; ++i) {
				this.addSlot(new Slot(playerInventory, i, pos.x() + i * 18, pos.y()));
			}
		});
	}

	@UnknownNullability
	public Slot getSlot(final IItemHandler inv, final int index) {
		for (final Slot slot : this.slots) {
			if (slot.getContainerSlot() != index) {
				continue;
			}
			if (slot instanceof final SlotItemHandler slot2 && slot2.getItemHandler() == inv) {
				return slot;
			}
			if (slot instanceof final RepositionableSlotItemHandler slot2 && slot2.getItemHandler() == inv) {
				return slot;
			}
		}
		return null;
	}

	@Override
	public ItemStack quickMoveStack(final Player player, final int i) {
		//TODO implement
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(final Player player) {
		return AbstractContainerMenu.stillValid(this.access, player, this.machine.getMachineType().getBlock().get());
	}
}
