package conductance.api.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.ApiStatus;
import conductance.api.machine.gui.MachineMenu;

@SuppressWarnings("NotNullFieldNotInitialized")
@ApiStatus.Internal
public final class Internal {

	public static BiConsumer<MachineMenu, Consumer<ValueOutput>> MACHINE_SCREEN_PACKET_SENDER;
	public static Function<MachineMenu, Consumer<ValueInput>> MACHINE_SCREEN_PACKET_RECEIVER;

	private Internal() {
	}
}
