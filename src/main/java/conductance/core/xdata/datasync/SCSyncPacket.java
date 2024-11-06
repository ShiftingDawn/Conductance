package conductance.core.xdata.datasync;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import lombok.NoArgsConstructor;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.api.IManaged;
import conductance.api.machine.xdata.FriendlyNetworkInput;
import conductance.api.machine.xdata.FriendlyNetworkOutput;
import conductance.Conductance;

@NoArgsConstructor
public final class SCSyncPacket implements CustomPacketPayload {

	public static final ResourceLocation ID = Conductance.id("sc_block_entity_sync_packet");
	public static final Type<SCSyncPacket> TYPE = new Type<>(SCSyncPacket.ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, SCSyncPacket> CODEC = StreamCodec.ofMember(SCSyncPacket::serialize, SCSyncPacket::deserialize);
	private BlockPos blockPos;
	private IManaged managed;
	private boolean forceSync;
	private RegistryFriendlyByteBuf buffer;

	public SCSyncPacket(final BlockPos blockPos, final IManaged managed, final boolean forceSync) {
		this.blockPos = blockPos;
		this.managed = managed;
		this.forceSync = forceSync;
	}

	public void serialize(final RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(this.blockPos);
		buf.writeBoolean(this.forceSync);
		this.managed.getDataMap().toNetwork(this.forceSync ? Operation.FULL : Operation.PARTIAL, new FriendlyNetworkOutput(this.buffer));
	}

	public static SCSyncPacket deserialize(final RegistryFriendlyByteBuf buf) {
		final SCSyncPacket result = new SCSyncPacket(buf.readBlockPos(), null, false);
		result.forceSync = buf.readBoolean();
		result.buffer = buf;
		return result;
	}

	public static void handle(final SCSyncPacket packet, final IPayloadContext ctx) {
		final Level level = Minecraft.getInstance().level;
		if (level != null && level.getBlockEntity(packet.blockPos) instanceof final IManaged managed) {
			managed.getDataMap().fromNetwork(packet.forceSync ? Operation.FULL : Operation.PARTIAL, new FriendlyNetworkInput(packet.buffer));
		}
	}

	public static SCSyncPacket of(final BlockEntity blockEntity, final boolean forceSync) {
		return new SCSyncPacket(blockEntity.getBlockPos(), (IManaged) blockEntity, forceSync);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return SCSyncPacket.TYPE;
	}
}
