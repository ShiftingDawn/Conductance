package conductance.api.util;

import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineMenu;

@SuppressWarnings("NotNullFieldNotInitialized")
@ApiStatus.Internal
public final class Internal {

	public static TriConsumer<MachineMenu, @Nullable ServerPlayer, Consumer<ValueOutput>> MACHINE_SCREEN_PACKET_SENDER;
	public static Function<MachineMenu, Consumer<ValueInput>> MACHINE_SCREEN_PACKET_RECEIVER_SERVER;
	public static Function<MachineMenu, Consumer<ValueInput>> MACHINE_SCREEN_PACKET_RECEIVER_CLIENT;

	private Internal() {
	}
}
