package conductance.api.plugin;

import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.material.PeriodicElement;
import conductance.api.util.TextHelper;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterPeriodicElementEvent implements IConductancePluginEvent {

	public interface PeriodicElementRegister {

		PeriodicElement create(long protons, long neutrons, String registryName, String name, String symbol, @Nullable PeriodicElement parent);
	}

	private final PeriodicElementRegister delegate;

	public PeriodicElement create(final long protons, final long neutrons, final String registryName, final String name, final String symbol, @Nullable final PeriodicElement parent) {
		return this.delegate.create(protons, neutrons, registryName, name, symbol, parent);
	}

	public PeriodicElement create(final long protons, final long neutrons, final String name, final String symbol, @Nullable final PeriodicElement parent) {
		return this.create(protons, neutrons, TextHelper.toLowerCaseUnderscore(name.replaceAll("-", "")), name, symbol, parent);
	}
}
