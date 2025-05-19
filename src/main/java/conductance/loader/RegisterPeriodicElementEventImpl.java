package conductance.loader;

import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import conductance.api.material.PeriodicElement;
import conductance.api.plugin.RegisterPeriodicElementEvent;

@AllArgsConstructor
final class RegisterPeriodicElementEventImpl implements RegisterPeriodicElementEvent {

	public interface PeriodicElementRegister {

		PeriodicElement create(long protons, long neutrons, String registryName, String name, String symbol, @Nullable PeriodicElement parent);
	}

	private final PeriodicElementRegister delegate;

	@Override
	public PeriodicElement create(final long protons, final long neutrons, final String registryName, final String name, final String symbol, @Nullable final PeriodicElement parent) {
		return this.delegate.create(protons, neutrons, registryName, name, symbol, parent);
	}
}
