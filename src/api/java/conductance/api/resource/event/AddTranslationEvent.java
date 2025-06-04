package conductance.api.resource.event;

import conductance.api.plugin.IConductancePluginEvent;

public interface AddTranslationEvent extends IConductancePluginEvent {

	void add(String key, String value);
}
