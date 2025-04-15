package conductance.api.plugin;

import conductance.api.util.tier.Tier;

public interface TierRegister {

	Tier.Builder register(String registryName, String displayName, int tierColor);
}
