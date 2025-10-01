package conductance.api.machine.gui;

import java.util.function.Consumer;
import net.minecraft.world.level.storage.ValueOutput;

interface WidgetPacketHandler {

	void sendRequest(GuiWidget widget, int requestId, Consumer<ValueOutput> packetFiller);
}
