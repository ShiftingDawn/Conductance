package conductance.core.data.datasync;

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
import conductance.api.machine.data.ISynchronized;
import conductance.Conductance;

@NoArgsConstructor
public final class SCSyncPacket implements CustomPacketPayload {

	public static final ResourceLocation ID = Conductance.id("sc_block_entity_sync_packet");
	public static final Type<SCSyncPacket> TYPE = new Type<>(SCSyncPacket.ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, SCSyncPacket> CODEC = StreamCodec.ofMember(SCSyncPacket::serialize, SCSyncPacket::deserialize);
	private BlockPos blockPos;
	private ISynchronized managed;
	private boolean forceSync;
	private RegistryFriendlyByteBuf buffer;

	public SCSyncPacket(final BlockPos blockPos, final ISynchronized managed, final boolean forceSync) {
		this.blockPos = blockPos;
		this.managed = managed;
		this.forceSync = forceSync;
	}

	public void serialize(final RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(this.blockPos);
		this.managed.getDataMap().writeToNetwork(buf, this.forceSync);
	}

	public static SCSyncPacket deserialize(final RegistryFriendlyByteBuf buf) {
		final SCSyncPacket result = new SCSyncPacket(buf.readBlockPos(), null, false);
		result.buffer = buf;
		return result;
	}

	public static void handle(final SCSyncPacket packet, final IPayloadContext ctx) {
		final Level level = Minecraft.getInstance().level;
		if (level != null && level.getBlockEntity(packet.blockPos) instanceof final ISynchronized iSynchronized) {
			iSynchronized.getDataMap().readFromNetwork(packet.buffer);
		}
	}

	public static SCSyncPacket of(final BlockEntity blockEntity, final boolean forceSync) {
		return new SCSyncPacket(blockEntity.getBlockPos(), (ISynchronized) blockEntity, forceSync);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return SCSyncPacket.TYPE;
	}
}
