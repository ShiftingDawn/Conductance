package conductance.core.sync;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import conductance.api.machine.sync.SyncHelper;
import conductance.api.plugin.RegisterFieldSerializerEvent;
import conductance.Conductance;
import conductance.loader.PluginEventBus;

public final class SyncCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.setApiValue(SyncHelper.class, SyncHelperImpl.INSTANCE);
		modEventBus.addListener(FMLLoadCompleteEvent.class, ignored -> SyncFieldSerializerRegisterImpl.INSTANCE.freeze());
		PluginEventBus.postAll(RegisterFieldSerializerEvent.class, new RegisterFieldSerializerEventImpl(SyncFieldSerializerRegisterImpl.INSTANCE));
	}

	private SyncCore() {
	}
}
