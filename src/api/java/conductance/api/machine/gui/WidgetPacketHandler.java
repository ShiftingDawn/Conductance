package conductance.api.machine.gui;

import java.util.function.Consumer;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

interface WidgetPacketHandler {

	void sendPacket(IGuiWidget widget, int requestId, @Nullable Consumer<ValueOutput> packetFiller);
}
