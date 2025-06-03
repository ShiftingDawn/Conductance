package conductance.api.tier.event;

import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.tier.Tier;

public interface RegisterTierEvent extends IConductancePluginEvent {

	Tier register(String registryName, String displayName, int tierColor, Tier previousTier);

	Tier register(String registryName, String displayName, int tierColor);
}
