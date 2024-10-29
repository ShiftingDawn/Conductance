package conductance.api.plugin;

import javax.annotation.Nullable;
import conductance.api.material.PeriodicElement;
import conductance.api.util.TextHelper;

public interface PeriodicElementBuilder {

	PeriodicElement create(final long protons, final long neutrons, final String registryName, final String name, final String symbol, @Nullable final PeriodicElement parent);

	default PeriodicElement create(final long protons, final long neutrons, final String name, final String symbol, @Nullable final PeriodicElement parent) {
		return this.create(protons, neutrons, TextHelper.toLowerCaseUnderscore(name.replaceAll("-", "")), name, symbol, parent);
	}
}
