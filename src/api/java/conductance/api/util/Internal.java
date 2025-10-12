package conductance.api.util;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.multi.IMultiBlockController;

@SuppressWarnings("NotNullFieldNotInitialized")
@ApiStatus.Internal
public final class Internal {

	public static TriConsumer<MachineMenu, @Nullable ServerPlayer, Consumer<ValueOutput>> MACHINE_SCREEN_PACKET_SENDER;
	public static Function<MachineMenu, Consumer<ValueInput>> MACHINE_SCREEN_PACKET_RECEIVER_SERVER;
	public static Function<MachineMenu, Consumer<ValueInput>> MACHINE_SCREEN_PACKET_RECEIVER_CLIENT;
	public static TriConsumer<MachineBlockEntity<?>, @Nullable ServerLevel, Consumer<ValueOutput>> MACHINE_RPC_PACKET_SENDER;
	public static Function<MachineBlockEntity<?>, Consumer<ValueInput>> MACHINE_RPC_PACKET_RECEIVER_SERVER;
	public static Function<MachineBlockEntity<?>, Consumer<ValueInput>> MACHINE_RPC_PACKET_RECEIVER_CLIENT;
	public static BiConsumer<ServerLevel, IMultiBlockController<?>> MULTIBLOCK_CONTROLLER_LOAD;
	public static BiConsumer<ServerLevel, IMultiBlockController<?>> MULTIBLOCK_CONTROLLER_UNLOAD;
	public static BooleanSupplier IS_EXPLOSION_ENABLED = () -> false; //Gets overridden by main mod to use config supplied value

	private Internal() {
	}
}
