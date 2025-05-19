package conductance.api.plugin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.util.tier.Tier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterTierEvent implements IConductancePluginEvent {

	public interface TierRegister {

		Tier register(String registryName, String displayName, int tierColor, Tier previousTier);

		Tier register(String registryName, String displayName, int tierColor);
	}

	private final TierRegister delegate;

	public Tier register(final String registryName, final String displayName, final int tierColor, final Tier previousTier) {
		return this.delegate.register(registryName, displayName, tierColor, previousTier);
	}

	public Tier register(final String registryName, final String displayName, final int tierColor) {
		return this.delegate.register(registryName, displayName, tierColor);
	}
}
