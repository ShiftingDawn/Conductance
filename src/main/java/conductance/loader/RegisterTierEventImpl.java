package conductance.loader;

import lombok.AllArgsConstructor;
import conductance.api.plugin.RegisterTierEvent;
import conductance.api.util.tier.Tier;

@AllArgsConstructor
final class RegisterTierEventImpl implements RegisterTierEvent {

	public interface TierRegister {

		Tier register(String registryName, String displayName, int tierColor, Tier previousTier);

		Tier register(String registryName, String displayName, int tierColor);
	}

	private final TierRegister delegate;

	@Override
	public Tier register(final String registryName, final String displayName, final int tierColor, final Tier previousTier) {
		return this.delegate.register(registryName, displayName, tierColor, previousTier);
	}

	@Override
	public Tier register(final String registryName, final String displayName, final int tierColor) {
		return this.delegate.register(registryName, displayName, tierColor);
	}
}
