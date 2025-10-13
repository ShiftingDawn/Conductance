package conductance.core.machine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.machine.multi.IMultiBlockController;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;
import conductance.Conductance;

public final class MultiBlockSavedData extends SavedData {

	public static final SavedDataType<MultiBlockSavedData> ID = new SavedDataType<>(
		"%s_multiblocks".formatted(CAPI.MOD_ID),
		MultiBlockSavedData::new,
		ctx -> RecordCodecBuilder.create(instance -> instance.group(
			RecordCodecBuilder.point(ctx)
		).apply(instance, MultiBlockSavedData::new))
	);
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat(Conductance.MODID + " multiblock checker thread (%d)").setDaemon(true).build();
	private final List<IMultiBlockController<?>> controllers = new CopyOnWriteArrayList<>();
	private final ServerLevel level;
	private @Nullable ScheduledExecutorService executor;

	private MultiBlockSavedData(final SavedData.Context ctx) {
		this.level = ctx.levelOrThrow();
	}

	public void addController(final IMultiBlockController<?> controller) {
		if (this.controllers.contains(controller)) {
			Conductance.LOGGER.error("Trying to add duplicate multiblock {} from structure checker thread", controller.getMultiBlockInfo().controllerPos());
			return;
		}
		this.controllers.add(controller);
		this.initialize();
	}

	public void removeController(final IMultiBlockController<?> controller) {
		if (!this.controllers.contains(controller)) {
			Conductance.LOGGER.error("Trying to remove unknown multiblock {} from structure checker thread", controller.getMultiBlockInfo().controllerPos());
			return;
		}
		this.controllers.remove(controller);
		if (this.controllers.isEmpty()) {
			this.shutdown();
		}
	}

	private void tick() {
		if (!Conductance.isSafeToAccessLevel()) {
			return;
		}
		for (final IMultiBlockController<?> controller : this.controllers) {
			try {
				MultiControllerMachineBlockEntity.checkStructure(this.level, controller);
			} catch (final Throwable e) {
				Conductance.LOGGER.error("Error while validating multiblock structure {}: {}", controller.getMultiBlockInfo().controllerPos(), e);
			}
		}
	}

	private void initialize() {
		if (this.executor != null && !this.executor.isShutdown()) {
			return;
		}
		this.executor = Executors.newSingleThreadScheduledExecutor(MultiBlockSavedData.THREAD_FACTORY);
		this.executor.scheduleAtFixedRate(this::tick, 0, 2, TimeUnit.SECONDS);
	}

	private void shutdown() {
		if (this.executor == null) {
			return;
		}
		this.executor.shutdownNow();
		this.executor = null;
	}

	public static MultiBlockSavedData getOrCreate(final ServerLevel serverLevel) {
		return serverLevel.getDataStorage().computeIfAbsent(MultiBlockSavedData.ID);
	}

	static void onMultiBlockControllerLoad(final ServerLevel level, final IMultiBlockController<?> controller) {
		MultiBlockSavedData.getOrCreate(level).addController(controller);
	}

	static void onMultiBlockControllerUnload(final ServerLevel level, final IMultiBlockController<?> controller) {
		MultiBlockSavedData.getOrCreate(level).removeController(controller);
	}
}
