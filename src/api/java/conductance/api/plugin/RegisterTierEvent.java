package conductance.api.plugin;

import conductance.api.util.tier.Tier;

public interface RegisterTierEvent extends IConductancePluginEvent {

	Tier register(String registryName, String displayName, int tierColor, Tier previousTier);

	Tier register(String registryName, String displayName, int tierColor);
}
