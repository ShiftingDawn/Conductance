package conductance.core.sync;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import lombok.RequiredArgsConstructor;
import conductance.api.sync.IManaged;
import conductance.api.sync.Operation;
import conductance.Conductance;

@RequiredArgsConstructor
public final class S2CSyncPacket implements CustomPacketPayload {

	public static final ResourceLocation ID = Conductance.id("sc_block_entity_sync_packet");
	public static final Type<S2CSyncPacket> TYPE = new Type<>(S2CSyncPacket.ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncPacket> CODEC = StreamCodec.ofMember(S2CSyncPacket::serialize, S2CSyncPacket::new);
	private final BlockPos blockPos;
	private final CompoundTag data;
	private final boolean forceSync;

	public S2CSyncPacket(final RegistryFriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readNbt(), buf.readBoolean());
	}

	public void serialize(final RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(this.blockPos);
		buf.writeNbt(this.data);
		buf.writeBoolean(this.forceSync);
	}

	public static void handle(final S2CSyncPacket packet, final IPayloadContext ctx) {
		final Level level = Minecraft.getInstance().level;
		if (level != null && level.getBlockEntity(packet.blockPos) instanceof final IManaged managed) {
			final HolderLookup.Provider registries = ((BlockEntity) managed).getLevel().registryAccess();
			managed.getDataMap().deserialize(packet.forceSync ? Operation.NETWORK_FULL : Operation.NETWORK_PARTIAL, packet.data, registries);
		}
	}

	public static S2CSyncPacket of(final BlockEntity blockEntity, final IManaged managed, final boolean forceSync) {
		final Operation operation = forceSync ? Operation.NETWORK_FULL : Operation.NETWORK_PARTIAL;
		final CompoundTag tag = managed.getDataMap().serialize(operation, blockEntity.getLevel().registryAccess());
		return new S2CSyncPacket(blockEntity.getBlockPos(), tag, forceSync);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return S2CSyncPacket.TYPE;
	}
}
