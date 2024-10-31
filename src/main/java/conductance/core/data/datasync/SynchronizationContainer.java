package conductance.core.data.datasync;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import conductance.api.machine.data.IManaged;
import conductance.core.data.ManagedDataMapImpl;

public final class SynchronizationContainer implements SyncTask {

	private static final Map<BlockEntity, SynchronizationContainer> CONTAINERS = new WeakHashMap<>();
	private final BlockEntity blockEntity;
	private final IManaged managed;

	public SynchronizationContainer(final BlockEntity blockEntity) {
		this.blockEntity = blockEntity;
		this.managed = (IManaged) blockEntity;
	}

	@Override
	public void tick(final long tick, final ServerLevel serverLevel) {
		if (ServerLifecycleHooks.getCurrentServer() != null && !this.blockEntity.isRemoved()) {
			final ManagedDataMapImpl map = (ManagedDataMapImpl) this.managed.getDataMap();
			map.tick();
			if (map.shouldSync()) {
				ServerLifecycleHooks.getCurrentServer().execute(() -> {
					final SCSyncPacket packet = SCSyncPacket.of(this.blockEntity, false);
					PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(this.blockEntity.getBlockPos()), packet);
				});
			}
		}
	}

	public static void dispatch(final BlockEntity blockEntity) {
		if (blockEntity.getLevel() instanceof final ServerLevel serverLevel && !SynchronizationContainer.CONTAINERS.containsKey(blockEntity)) {
			final SynchronizationContainer container = SynchronizationContainer.CONTAINERS.computeIfAbsent(blockEntity, SynchronizationContainer::new);
			SynchronizationManager.getInstance(serverLevel).addTask(container);
		}
	}

	public static void destroy(final BlockEntity blockEntity) {
		if (blockEntity.getLevel() instanceof final ServerLevel serverLevel) {
			final SynchronizationContainer container = SynchronizationContainer.CONTAINERS.get(blockEntity);
			if (container != null) {
				SynchronizationManager.getInstance(serverLevel).stopTask(container);
				SynchronizationContainer.CONTAINERS.remove(blockEntity);
			}
		}
	}
}
