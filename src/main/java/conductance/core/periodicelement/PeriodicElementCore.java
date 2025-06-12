package conductance.core.periodicelement;

import net.minecraft.resources.ResourceLocation;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.periodicelement.event.RegisterPeriodicElementEvent;
import conductance.core.register.RegisterCore;
import conductance.loader.PluginEventBus;

public final class PeriodicElementCore {

	public static void initialize() {
		PluginEventBus.post(RegisterPeriodicElementEvent.class, modid -> new RegisterPeriodicElementEventImpl((protons, neutrons, registryName, name, symbol, parent) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final PeriodicElement result = new PeriodicElement(registryKey, protons, neutrons, name, symbol, parent != null ? parent.getRegistryKey() : null);
			RegisterCore.getRegs().periodicElements().register(result);
			return result;
		}));
	}

	private PeriodicElementCore() {
	}
}
