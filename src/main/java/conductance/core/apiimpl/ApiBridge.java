package conductance.core.apiimpl;

import conductance.Conductance;
import conductance.api.CAPI;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ApiBridge {

	public static final ConductanceRegistryImpl<ResourceLocation, ConductanceRegistryImpl<?, ?>> REGISTRIES = new ConductanceRegistryImpl.ResourceKeyed<>(Conductance.id("root"));
	public static final RegistryProviderImpl REGS = new RegistryProviderImpl();

	public static void init() {
		CAPI.REGS = ApiBridge.REGS;
	}

	@SubscribeEvent
	private static void onLoadComplete(final FMLLoadCompleteEvent ignored) {
		ApiBridge.REGISTRIES.freeze();
		ApiBridge.REGISTRIES.values().forEach(ConductanceRegistryImpl::freeze);
	}
}