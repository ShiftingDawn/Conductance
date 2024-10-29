package conductance.api;

import conductance.api.plugin.PeriodicElementBuilder;

public interface IConductancePlugin {

	default void registerPeriodicElements(final PeriodicElementBuilder builder) {
	}
}
