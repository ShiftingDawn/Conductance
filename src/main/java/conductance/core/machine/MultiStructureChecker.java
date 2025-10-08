package conductance.core.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;
import conductance.Conductance;

final class MultiStructureChecker {

	private static final Map<ServerLevel, MultiStructureChecker> INSTANCES = new IdentityHashMap<>();
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat(Conductance.MODID + " multiblock checker thread (%d)").setDaemon(true).build();
	private final List<IMultiBlockController<?>> controllers = Collections.synchronizedList(new ArrayList<>());
	private final ServerLevel level;
	private @Nullable ScheduledExecutorService executor;

	private MultiStructureChecker(final ServerLevel level) {
		this.level = level;
		NeoForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	private void onLevelUnload(final LevelEvent.Unload event) {
		if (event.getLevel() == this.level) {
			MultiStructureChecker.INSTANCES.remove(this.level);
			NeoForge.EVENT_BUS.unregister(this);
		}
	}

	private void initialize() {
		if (this.executor != null && !this.executor.isShutdown()) {
			return;
		}
		this.executor = Executors.newSingleThreadScheduledExecutor(MultiStructureChecker.THREAD_FACTORY);
		this.executor.scheduleAtFixedRate(this::tick, 0, 50, TimeUnit.MILLISECONDS);
	}

	private void shutdown() {
		if (this.executor == null) {
			return;
		}
		this.executor.shutdownNow();
		this.executor = null;
	}

	public void addController(final IMultiBlockController<?> controller) {
		this.controllers.add(controller);
		this.initialize();
	}

	public void removeController(final IMultiBlockController<?> controller) {
		if (this.controllers.contains(controller)) {
			this.controllers.remove(controller);
			if (this.controllers.isEmpty()) {
				this.shutdown();
			}
		}
	}

	private void tick() {
		for (final IMultiBlockController<?> controller : this.controllers) {
			try {
				MultiControllerMachineBlockEntity.checkStructure(controller, false);
			} catch (final Throwable e) {
				Conductance.LOGGER.error("Error while validating multiblock structure {}: {}", controller.getMultiBlockInfo().controllerPos(), e.getMessage());
			}
		}
	}

	static void onMultiBlockControllerLoad(final ServerLevel level, final IMultiBlockController<?> controller) {
		MultiStructureChecker.INSTANCES.computeIfAbsent(level, MultiStructureChecker::new).addController(controller);
	}

	static void onMultiBlockControllerUnload(final ServerLevel level, final IMultiBlockController<?> controller) {
		final MultiStructureChecker checker = MultiStructureChecker.INSTANCES.get(level);
		if (checker != null) {
			checker.removeController(controller);
		}
	}
}
