package conductance.api.machine.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.util.Internal;
import conductance.api.util.Lazy;

public class MachineMenu extends AbstractContainerMenu {

	public static final Supplier<MenuType<?>> MENU_TYPE = Lazy.of(() -> BuiltInRegistries.MENU.getValue(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "machine")));

	private final @Getter(AccessLevel.PACKAGE) Map<String, GuiWidget> widgets = new HashMap<>();
	private final @Getter(AccessLevel.PACKAGE) Map<GuiWidget, String> widgetsReversed = new HashMap<>();
	private final @Getter MachineBlockEntity<?> machine;
	private final @Getter ContainerLevelAccess access;
	private final @Getter Inventory playerInventory;
	private final @Getter GuiSetup guiSetup;

	public MachineMenu(final MachineBlockEntity<?> machine, final int containerId, final ContainerLevelAccess access, final Inventory playerInventory) {
		super(MachineMenu.MENU_TYPE.get(), containerId);
		this.machine = machine;
		this.access = access;
		this.playerInventory = playerInventory;
		this.guiSetup = Objects.requireNonNull(machine.getMachineType().getGuiSetup(), "Opened a menu for a machine without a GuiSetup!");
		this.guiSetup.addSlots(this, this::addSlot);
		Optional.ofNullable(this.guiSetup.getPlayerInventoryPos(this.guiSetup.getContainerSize())).ifPresent(pos -> {
			for (int y = 0; y < 3; ++y) {
				for (int x = 0; x < 9; ++x) {
					this.addSlot(new Slot(playerInventory, x + (y + 1) * 9, pos.x() + x * 18, pos.y() + y * 18));
				}
			}
		});
		Optional.ofNullable(this.guiSetup.getPlayerHotbarPos(this.guiSetup.getContainerSize())).ifPresent(pos -> {
			for (int i = 0; i < 9; ++i) {
				this.addSlot(new Slot(playerInventory, i, pos.x() + i * 18, pos.y()));
			}
		});
		this.guiSetup.addWidgets(this, this::addWidget);
	}

	public final <T extends GuiWidget> T addWidget(final String id, final T widget) {
		this.widgets.put(id, widget);
		this.widgetsReversed.put(widget, id);
		widget.setMenu(() -> this);
		if (this.playerInventory.player instanceof final ServerPlayer serverPlayer) {
			widget.setWidgetPacketHandler(this.sendToClient(serverPlayer));
		}
		return widget;
	}

	private WidgetPacketHandler sendToClient(final ServerPlayer player) {
		return (widget, requestId, packetFiller) -> {
			Internal.MACHINE_SCREEN_PACKET_SENDER.accept(this, player, contentFactory -> {
				final String key = Objects.requireNonNull(this.getWidgetId(widget), "Cannot send client request for unknown widget.");
				contentFactory.putString("w", key);
				contentFactory.putInt("r", requestId);
				packetFiller.accept(contentFactory.child("d"));
			});
		};
	}

	public final @UnknownNullability GuiWidget getWidgetById(final String id) {
		GuiWidget result = this.widgets.get(id);
		if (result == null) {
			for (final GuiWidget widget : this.widgets.values()) {
				if (widget instanceof final WidgetGroup group) {
					result = group.getWidgetById(id);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return result;
	}

	public final @UnknownNullability String getWidgetId(final GuiWidget widget) {
		String result = this.widgetsReversed.get(widget);
		if (result == null) {
			for (final GuiWidget childWidget : this.widgets.values()) {
				if (childWidget instanceof final WidgetGroup group) {
					result = group.getWidgetId(widget);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return result;
	}

	public @UnknownNullability Slot getSlot(final IItemHandler inv, final int index) {
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

	@SuppressWarnings("unused")
	private void handlePacket(final boolean isServer, final ValueInput input) {
		final String key = input.getString("w").orElseThrow(() -> new IllegalStateException("Missing widget key"));
		final int req = input.getInt("r").orElseThrow(() -> new IllegalStateException("Missing request id"));
		final ValueInput data = input.childOrEmpty("d");
		final GuiWidget widget = Objects.requireNonNull(this.widgets.get(key), "Invalid widget key");
		if (isServer) {
			widget.handleClientRequest(req, data);
		} else {
			widget.handleServerRequest(req, data);
		}
	}

	private void handleClientRequestPacket(final ValueInput input) {
		this.handlePacket(true, input);
	}

	private void handleServerRequestPacket(final ValueInput input) {
		this.handlePacket(true, input);
	}

	static {
		Internal.MACHINE_SCREEN_PACKET_RECEIVER_SERVER = machine -> machine::handleClientRequestPacket;
		Internal.MACHINE_SCREEN_PACKET_RECEIVER_CLIENT = machine -> machine::handleServerRequestPacket;
	}
}
