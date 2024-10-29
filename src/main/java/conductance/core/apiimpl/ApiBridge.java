package conductance.core.apiimpl;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import conductance.api.CAPI;
import conductance.Conductance;
import conductance.core.ConductanceRegistrate;
import conductance.core.register.MaterialRegistry;

@EventBusSubscriber(modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ApiBridge {

	public enum DataPackRegistryLoadStage {
		UNFREEZE, RESET, REFREEZE
	}

	public static final ConductanceRegistryImpl<ResourceLocation, ConductanceRegistryImpl<?, ?>> REGISTRIES = new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id("root"));
	public static final RegistryProviderImpl REGS = new RegistryProviderImpl();
	public static ConductanceRegistrate registrate;

	public static void init(IEventBus modEventBus) {
		ApiBridge.registrate = ConductanceRegistrate.create(modEventBus);
		CAPI.REGS = ApiBridge.REGS;
		CAPI.RESOURCE_FINDER = new ResourceFinderImpl();
		CAPI.MATERIALS = MaterialRegistry.INSTANCE;
	}

	@SubscribeEvent
	private static void onLoadComplete(final FMLLoadCompleteEvent ignored) {
		ApiBridge.REGISTRIES.freeze();
		ApiBridge.REGISTRIES.values().forEach(ConductanceRegistryImpl::freeze);
	}

	public static void handleDataPackRegistryStage(final DataPackRegistryLoadStage stage) {
		switch (stage) {
			case UNFREEZE	-> ApiBridge.REGISTRIES.values().forEach(reg -> {
							if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
								dataPackRegistry.unfreeze();
							}
						});
			case RESET		-> ApiBridge.REGISTRIES.values().forEach(reg -> {
							if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
								dataPackRegistry.reset();
							}
						});
			case REFREEZE	-> ApiBridge.REGISTRIES.values().forEach(reg -> {
							if (reg instanceof final ConductanceDataPackRegistry<?> dataPackRegistry) {
								dataPackRegistry.freeze();
							}
						});
		}
	}
}
