package conductance.core.machine;

import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.util.Internal;
import conductance.Conductance;

public record MachineScreenRequestPacket(int containerId, CompoundTag nbt) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<MachineScreenRequestPacket> TYPE = new CustomPacketPayload.Type<>(Conductance.id("machine_gui_request"));
	public static final StreamCodec<ByteBuf, MachineScreenRequestPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, MachineScreenRequestPacket::containerId,
		ByteBufCodecs.COMPOUND_TAG, MachineScreenRequestPacket::nbt,
		MachineScreenRequestPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return MachineScreenRequestPacket.TYPE;
	}

	public static void register(final PayloadRegistrar registrar) {
		registrar.playBidirectional(MachineScreenRequestPacket.TYPE, MachineScreenRequestPacket.STREAM_CODEC, MachineScreenRequestPacket::handleOnServer, MachineScreenRequestPacket::handleOnClient);
		Internal.MACHINE_SCREEN_PACKET_SENDER = MachineScreenRequestPacket::send;
	}

	private static void send(final MachineMenu machineMenu, @Nullable final ServerPlayer player, final Consumer<ValueOutput> payloadFactory) {
		try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
			final TagValueOutput output = TagValueOutput.createWithContext(reporter, machineMenu.getMachine().getLevel().registryAccess());
			payloadFactory.accept(output);
			final MachineScreenRequestPacket packet = new MachineScreenRequestPacket(machineMenu.containerId, output.buildResult());
			if (player == null) {
				ClientPacketDistributor.sendToServer(packet);
			} else {
				PacketDistributor.sendToPlayer(player, packet);
			}
		}
	}

	private static void handleOnServer(final MachineScreenRequestPacket packet, final IPayloadContext ctx) {
		if (ctx.player().containerMenu instanceof final MachineMenu menu) {
			try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
				final ValueInput input = TagValueInput.create(reporter, ctx.player().registryAccess(), packet.nbt);
				Internal.MACHINE_SCREEN_PACKET_RECEIVER_SERVER.apply(menu).accept(input);
			}
		}
	}

	private static void handleOnClient(final MachineScreenRequestPacket packet, final IPayloadContext ctx) {
		if (ctx.player().containerMenu instanceof final MachineMenu menu) {
			try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
				final ValueInput input = TagValueInput.create(reporter, ctx.player().registryAccess(), packet.nbt);
				Internal.MACHINE_SCREEN_PACKET_RECEIVER_CLIENT.apply(menu).accept(input);
			}
		}
	}
}
