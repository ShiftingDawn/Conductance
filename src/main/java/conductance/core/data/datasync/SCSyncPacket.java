package conductance.core.data.datasync;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
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
import conductance.api.CAPI;
import conductance.api.machine.data.IManaged;
import conductance.api.machine.data.ISynchronized;
import conductance.api.machine.data.serializer.DataSerializer;
import conductance.Conductance;
import conductance.core.data.BaseInstancedField;
import conductance.core.data.ManagedDataMapImpl;

@NoArgsConstructor
public final class SCSyncPacket implements CustomPacketPayload {

	public static final ResourceLocation ID = Conductance.id("sc_block_entity_sync_packet");
	public static final Type<SCSyncPacket> TYPE = new Type<>(SCSyncPacket.ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, SCSyncPacket> CODEC = StreamCodec.ofMember(SCSyncPacket::serialize, SCSyncPacket::deserialize);
	private BlockPos blockPos;
	private BitSet changedFieldSet;
	private DataSerializer<?>[] data;

	public SCSyncPacket(final BlockPos blockPos, final BitSet changedFieldSet, final DataSerializer<?>[] data) {
		this.blockPos = blockPos;
		this.changedFieldSet = changedFieldSet;
		this.data = data;
	}

	public void serialize(final RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(this.blockPos);
		buf.writeByteArray(this.changedFieldSet.toByteArray());
		for (final DataSerializer<?> serializer : this.data) {
			buf.writeVarInt(serializer.getId());
			serializer.toNetwork(buf);
		}
	}

	public static SCSyncPacket deserialize(final RegistryFriendlyByteBuf buf) {
		final BlockPos pos = buf.readBlockPos();
		final BitSet changedFieldSet = BitSet.valueOf(buf.readByteArray());
		final DataSerializer<?>[] data = new DataSerializer[changedFieldSet.cardinality()];
		for (int i = 0; i < data.length; ++i) {
			final int serializerId = buf.readVarInt();
			final DataSerializer<?> serializer = CAPI.managedDataRegistry().makeSerializer(serializerId);
			serializer.fromNetwork(buf);
			data[i] = serializer;
		}
		return new SCSyncPacket(pos, changedFieldSet, data);
	}

	public static void handle(final SCSyncPacket packet, final IPayloadContext ctx) {
		final Level level = Minecraft.getInstance().level;
		if (level != null && level.getBlockEntity(packet.blockPos) instanceof final ISynchronized iSynchronized) {
			final ManagedDataMapImpl map = (ManagedDataMapImpl) iSynchronized.getDataMap();
			int currentSerializer = 0;
			for (int i = 0; i < packet.changedFieldSet.length(); ++i) {
				if (packet.changedFieldSet.get(i)) {
					final DataSerializer<?> serializer = packet.data[currentSerializer++];
					map.deserializeBySyncId(i, level.registryAccess(), serializer);
				}
			}
		}
	}

	public static SCSyncPacket of(final BlockEntity blockEntity, final boolean forceSync) {
		final ManagedDataMapImpl map = (ManagedDataMapImpl) ((IManaged) blockEntity).getDataMap();

		final List<DataSerializer<?>> data = new ArrayList<>();
		final BitSet changedFieldSet = new BitSet();
		final BaseInstancedField[] fields = map.getSyncedFields();
		for (int i = 0; i < fields.length; ++i) {
			final BaseInstancedField field = fields[i];
			if (field.isDirty() || forceSync) {
				changedFieldSet.set(i);
				data.add(map.serializeBySyncId(i, blockEntity.getLevel().registryAccess()));
				field.markNotDirty();
			}
		}
		return new SCSyncPacket(blockEntity.getBlockPos(), changedFieldSet, data.toArray(DataSerializer[]::new));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return SCSyncPacket.TYPE;
	}
}
