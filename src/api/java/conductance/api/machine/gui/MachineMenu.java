package conductance.api.machine.gui;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
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
import conductance.api.machine.MachineCapability;
import conductance.api.util.Internal;
import conductance.api.util.Lazy;

public class MachineMenu extends AbstractContainerMenu implements WidgetHolder {

	public static final Supplier<MenuType<?>> MENU_TYPE = Lazy.of(() -> BuiltInRegistries.MENU.getValue(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "machine")));

	private final @Getter MachineBlockEntity<?> machine;
	private final @Getter ContainerLevelAccess access;
	private final @Getter Inventory playerInventory;
	private final @Getter GuiSetup guiSetup;
	private final @Getter(AccessLevel.PACKAGE) WidgetGroup rootWidget;

	public MachineMenu(final MachineBlockEntity<?> machine, final int containerId, final ContainerLevelAccess access, final Inventory playerInventory) {
		super(MachineMenu.MENU_TYPE.get(), containerId);
		this.machine = machine;
		this.access = access;
		this.playerInventory = playerInventory;
		this.guiSetup = Objects.requireNonNull(machine.getMachineType().getGuiSetup(), "Opened a menu for a machine without a GuiSetup!");
		this.rootWidget = new WidgetGroup(0, 0, this.guiSetup.getContainerSize().width(), this.guiSetup.getContainerSize().height());
		this.rootWidget.setMenu(() -> this);
		this.guiSetup.addSlots(this, this::addSlot);
		if (playerInventory.player instanceof final ServerPlayer serverPlayer) {
			this.rootWidget.setWidgetPacketHandler(this.sendToClient(serverPlayer));
		}
		this.guiSetup.addPlayerInventorySlots(playerInventory, this::addSlot);
		this.guiSetup.addWidgets(this, this::addWidget);
		CAPI.make(new WidgetGroup(3, 3, 0, 0), controlGroup -> {
			controlGroup.setLayout(new WidgetLayouts.VerticalList(16, 16, 2, true));
			this.guiSetup.addControlWidgets(this, controlGroup::addWidget);
			for (final MachineCapability capability : this.getMachine().getCapabilities().values()) {
				capability.addGuiControls(this, controlGroup::addWidget);
			}
			if (!controlGroup.getWidgets().isEmpty()) {
				this.addWidget("control_container", CAPI.make(new WidgetGroup(-20, 0, 20, controlGroup.getHeight() + 4), group -> {
					group.setBackground(this.guiSetup.getTheme().getSidePanel());
					group.setSize(MutableSize.of(new ManagedInt(20), new ManagedInt(null, () -> controlGroup.getHeight() + 6)));
					group.addWidget("controls", controlGroup);
				}));
			}
		});
		this.guiSetup.addPlayerInventoryWidget(this, this::addWidget);
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

	private WidgetPacketHandler sendToClient(final ServerPlayer player) {
		return (widget, requestId, packetFiller) -> {
			Internal.MACHINE_SCREEN_PACKET_SENDER.accept(this, player, contentFactory -> {
				contentFactory.putInt("r", requestId);
				if (packetFiller != null) {
					packetFiller.accept(contentFactory.child("d"));
				}
			});
		};
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

	public @UnknownNullability Slot getSlot(final Container inv, final int index) {
		for (final Slot slot : this.slots) {
			if (slot.getContainerSlot() == index && slot.container == inv) {
				return slot;
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private void handlePacket(final boolean isServer, final ValueInput input) {
		final int req = input.getInt("r").orElseThrow(() -> new IllegalStateException("Missing request id"));
		input.child("d").ifPresent(data -> {
			if (isServer) {
				this.rootWidget.handleClientRequest(req, data);
			} else {
				this.rootWidget.handleServerRequest(req, data);
			}
		});
	}

	private void handleClientRequestPacket(final ValueInput input) {
		this.handlePacket(true, input);
	}

	private void handleServerRequestPacket(final ValueInput input) {
		this.handlePacket(false, input);
	}

	//region WidgetHolder

	@Override
	public WidgetBindingInfo internalCreateBindingInfo(final IGuiWidget targetChild) {
		return this.rootWidget.internalCreateBindingInfo(targetChild);
	}

	@Override
	public Map<String, IGuiWidget> internalGetWidgets() {
		return this.rootWidget.internalGetWidgets();
	}

	@Override
	public Map<IGuiWidget, String> internalGetWidgetsReversed() {
		return this.rootWidget.internalGetWidgetsReversed();
	}
	//endregion

	static {
		Internal.MACHINE_SCREEN_PACKET_RECEIVER_SERVER = machine -> machine::handleClientRequestPacket;
		Internal.MACHINE_SCREEN_PACKET_RECEIVER_CLIENT = machine -> machine::handleServerRequestPacket;
	}
}
