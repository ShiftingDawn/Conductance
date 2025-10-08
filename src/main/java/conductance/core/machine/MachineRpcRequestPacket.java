package conductance.core.machine;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
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
import conductance.api.machine.MachineBlockEntity;
import conductance.api.util.Internal;
import conductance.Conductance;

public record MachineRpcRequestPacket(ResourceKey<Level> level, BlockPos pos, CompoundTag nbt) implements CustomPacketPayload {

	public static final Type<MachineRpcRequestPacket> TYPE = new Type<>(Conductance.id("machine_rpc"));
	public static final StreamCodec<ByteBuf, MachineRpcRequestPacket> STREAM_CODEC = StreamCodec.composite(
		ResourceKey.streamCodec(Registries.DIMENSION), MachineRpcRequestPacket::level,
		BlockPos.STREAM_CODEC, MachineRpcRequestPacket::pos,
		ByteBufCodecs.COMPOUND_TAG, MachineRpcRequestPacket::nbt,
		MachineRpcRequestPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return MachineRpcRequestPacket.TYPE;
	}

	public static void register(final PayloadRegistrar registrar) {
		registrar.playBidirectional(MachineRpcRequestPacket.TYPE, MachineRpcRequestPacket.STREAM_CODEC, MachineRpcRequestPacket::handleOnServer, MachineRpcRequestPacket::handleOnClient);
		Internal.MACHINE_RPC_PACKET_SENDER = MachineRpcRequestPacket::send;
	}

	private static void send(final MachineBlockEntity<?> machine, @Nullable final ServerLevel level, final Consumer<ValueOutput> payloadFactory) {
		try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
			final TagValueOutput output = TagValueOutput.createWithContext(reporter, machine.getLevel().registryAccess());
			payloadFactory.accept(output);
			final MachineRpcRequestPacket packet = new MachineRpcRequestPacket(machine.getLevel().dimension(), machine.getBlockPos(), output.buildResult());
			if (level == null) {
				ClientPacketDistributor.sendToServer(packet);
			} else {
				PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(machine.getBlockPos()), packet);
			}
		}
	}

	private static void handleOnServer(final MachineRpcRequestPacket packet, final IPayloadContext ctx) {
		final ServerLevel level = ctx.player().getServer() != null ? ctx.player().getServer().getLevel(packet.level) : null;
		if (level != null && level.getBlockEntity(packet.pos) instanceof final MachineBlockEntity<?> machine) {
			try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
				final ValueInput input = TagValueInput.create(reporter, ctx.player().registryAccess(), packet.nbt);
				Internal.MACHINE_RPC_PACKET_RECEIVER_SERVER.apply(machine).accept(input);
			}
		}
	}

	private static void handleOnClient(final MachineRpcRequestPacket packet, final IPayloadContext ctx) {
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension() == packet.level) {
			if (Minecraft.getInstance().level.getBlockEntity(packet.pos) instanceof final MachineBlockEntity<?> machine) {
				try (final ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Conductance.LOGGER)) {
					final ValueInput input = TagValueInput.create(reporter, ctx.player().registryAccess(), packet.nbt);
					Internal.MACHINE_RPC_PACKET_RECEIVER_CLIENT.apply(machine).accept(input);
				}
			}
		}
	}
}
