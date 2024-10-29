package conductance.api.plugin;

import javax.annotation.Nullable;
import conductance.api.material.PeriodicElement;
import conductance.api.util.TextHelper;

public interface PeriodicElementBuilder {

	PeriodicElement create(long protons, long neutrons, String registryName, String name, String symbol, @Nullable PeriodicElement parent);

	default PeriodicElement create(final long protons, final long neutrons, final String name, final String symbol, @Nullable final PeriodicElement parent) {
		return this.create(protons, neutrons, TextHelper.toLowerCaseUnderscore(name.replaceAll("-", "")), name, symbol, parent);
	}
}
