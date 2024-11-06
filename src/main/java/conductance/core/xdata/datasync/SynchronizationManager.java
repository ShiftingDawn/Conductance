package conductance.core.xdata.datasync;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

@EventBusSubscriber(modid = CAPI.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class SynchronizationManager {

	private static final WeakHashMap<ServerLevel, SynchronizationManager> MANAGERS = new WeakHashMap<>();
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("Conductance Sync Thread %d").setDaemon(true).build();
	private final List<SyncTask> tasks = new ArrayList<>();
	private final ServerLevel serverLevel;
	@Nullable
	private ScheduledExecutorService executorService;
	private long currentTick = 0;

	private SynchronizationManager(final ServerLevel serverLevel) {
		this.serverLevel = serverLevel;
	}

	public void addTask(final SyncTask task) {
		synchronized (this.tasks) {
			this.tasks.add(task);
			this.tryStartExecutor();
		}
	}

	public void stopTask(final SyncTask task) {
		synchronized (this.tasks) {
			this.tasks.remove(task);
			if (this.tasks.isEmpty()) {
				this.tryStopExecutor();
			}
		}
	}

	private void tryStartExecutor() {
		if (this.executorService == null || this.executorService.isShutdown()) {
			this.executorService = Executors.newSingleThreadScheduledExecutor(SynchronizationManager.THREAD_FACTORY);
			this.executorService.scheduleAtFixedRate(this::tick, 0, 50, TimeUnit.MILLISECONDS);
		}
	}

	private void tryStopExecutor() {
		if (this.executorService != null && !this.executorService.isShutdown()) {
			this.executorService.shutdownNow();
			this.executorService = null;
		}
	}

	private void tick() {
		if (!this.serverLevel.getServer().isCurrentlySaving()) {
			final long tickId = this.currentTick++;
			synchronized (this.tasks) {
				for (final SyncTask task : new ArrayList<>(this.tasks)) {
					task.tick(tickId, this.serverLevel);
				}
			}
		}
	}

	public static SynchronizationManager getInstance(final ServerLevel serverLevel) {
		return SynchronizationManager.MANAGERS.computeIfAbsent(serverLevel, SynchronizationManager::new);
	}

	@SubscribeEvent
	private static void onWorldUnload(final LevelEvent.Unload event) {
		if (event.getLevel() instanceof final ServerLevel serverLevel) {
			final SynchronizationManager manager = SynchronizationManager.MANAGERS.get(serverLevel);
			if (manager != null) {
				manager.tryStopExecutor();
				SynchronizationManager.MANAGERS.remove(serverLevel);
			}
		}
	}
}
