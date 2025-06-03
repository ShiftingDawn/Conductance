package conductance.api.tier.event;

import java.util.function.Supplier;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;

public interface RegisterTierEvent extends IConductancePluginEvent {

	Tier register(String registryName, String displayName, int tierColor, Supplier<TieredComponentMap> componentMapFactory, Tier previousTier);

	Tier register(String registryName, String displayName, int tierColor, Supplier<TieredComponentMap> componentMapFactory);
}
