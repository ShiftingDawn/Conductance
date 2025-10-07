package conductance.api.machine.gui;

import java.util.function.Supplier;

public record WidgetBindingInfo(
	WidgetPacketHandler packetHandler,
	Supplier<MachineMenu> menuSupplier,
	Supplier<MachineScreen> screenSupplier,
	Rectangle parentBounds
) {
}
