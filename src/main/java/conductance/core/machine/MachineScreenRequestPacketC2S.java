package conductance.core.machine;

import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import io.netty.buffer.ByteBuf;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.util.Internal;
import conductance.Conductance;
import conductance.lib.mixin.MachineMenuHandleClientPacketInvoker;

public record MachineScreenRequestPacketC2S(int containerId, CompoundTag nbt) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<MachineScreenRequestPacketC2S> TYPE = new CustomPacketPayload.Type<>(Conductance.id("machine_gui_request"));
	public static final StreamCodec<ByteBuf, MachineScreenRequestPacketC2S> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, MachineScreenRequestPacketC2S::containerId,
		ByteBufCodecs.COMPOUND_TAG, MachineScreenRequestPacketC2S::nbt,
		MachineScreenRequestPacketC2S::new
	);


	@Override
	public Type<? extends CustomPacketPayload> type() {
		return MachineScreenRequestPacketC2S.TYPE;
	}

	public static void register(final PayloadRegistrar registrar) {
		registrar.playToServer(MachineScreenRequestPacketC2S.TYPE, MachineScreenRequestPacketC2S.STREAM_CODEC, MachineScreenRequestPacketC2S::handle);
		Internal.MACHINE_SCREEN_PACKET_SENDER = MachineScreenRequestPacketC2S::send;
	}

	private static void send(final MachineMenu machineMenu, final Consumer<ValueOutput> payloadFactory) {
		try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
			final TagValueOutput output = TagValueOutput.createWithContext(reporter, machineMenu.getMachine().getLevel().registryAccess());
			payloadFactory.accept(output);
			ClientPacketDistributor.sendToServer(new MachineScreenRequestPacketC2S(machineMenu.containerId, output.buildResult()));
		}
	}

	private static void handle(final MachineScreenRequestPacketC2S packet, final IPayloadContext ctx) {
		if (ctx.player().containerMenu instanceof final MachineMenu menu) {
			try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
				final ValueInput input = TagValueInput.create(reporter, ctx.player().registryAccess(), packet.nbt);
				((MachineMenuHandleClientPacketInvoker) menu).invokeHandleClientPacket(input);
			}
		}
	}
}
