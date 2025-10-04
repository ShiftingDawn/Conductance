package conductance.core.periodicelement;

import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.periodicelement.event.RegisterPeriodicElementEvent;
import conductance.Conductance;

public final class PeriodicElementCore {

	public static void initialize() {
		Conductance.dispatch(RegisterPeriodicElementEvent.class, modid -> new RegisterPeriodicElementEventImpl((protons, neutrons, registryName, name, symbol, parent) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final ResourceLocation parentKey = parent != null ? Conductance.REGISTRIES.periodicElements().getKey(parent) : null;
			final PeriodicElement periodicElement = new PeriodicElement(protons, neutrons, name, symbol, parentKey);
			for (final PeriodicElement element : CAPI.regs().periodicElements()) {
				if (element.symbol().equals(symbol)) {
					throw new IllegalArgumentException("The symbol %s has already been used by periodic element %s".formatted(symbol, CAPI.regs().periodicElements().getKey(element)));
				}
			}
			Conductance.REGISTRIES.register(CAPI.regs().periodicElements(), registryKey, periodicElement);
			return periodicElement;
		}));
	}

	private PeriodicElementCore() {
	}
}
