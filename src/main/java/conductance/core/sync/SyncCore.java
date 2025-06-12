package conductance.core.sync;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import conductance.api.sync.IManaged;
import conductance.api.sync.SyncHelper;
import conductance.api.sync.event.RegisterFieldSerializerEvent;
import conductance.Conductance;

public final class SyncCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.setApiValue(SyncHelper.class, SyncHelperImpl.INSTANCE);
		modEventBus.addListener(FMLLoadCompleteEvent.class, ignored -> SyncFieldSerializerRegisterImpl.INSTANCE.freeze());
		Conductance.dispatchAll(RegisterFieldSerializerEvent.class, new RegisterFieldSerializerEventImpl(SyncFieldSerializerRegisterImpl.INSTANCE));
	}

	public static void setupBlockEntity(final BlockEntity blockEntity, final IManaged managed) {
		if (managed.getDataMap() instanceof final ManagedDataMapImpl map) {
			map.init();
			if (!map.getSyncFields().isEmpty()) {
				SynchronizationContainer.dispatch(blockEntity);
			}
		}
	}

	public static void destroyBlockEntity(final BlockEntity blockEntity, final IManaged managed) {
		if (managed.getDataMap() instanceof final ManagedDataMapImpl map && !map.getSyncFields().isEmpty()) {
			SynchronizationContainer.destroy(blockEntity);
		}
	}

	private SyncCore() {
	}
}
