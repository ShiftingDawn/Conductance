package conductance.core.tier;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.tier.event.RegisterTierEvent;
import conductance.api.tier.Tier;

@AllArgsConstructor
final class RegisterTierEventImpl implements RegisterTierEvent {

	public interface TierRegister {

		Tier register(String registryName, String displayName, int tierColor, @Nullable Tier previousTier);
	}

	private final TierRegister delegate;

	@Override
	public Tier register(final String registryName, final String displayName, final int tierColor, final Tier previousTier) {
		return this.delegate.register(registryName, displayName, tierColor, previousTier);
	}

	@Override
	public Tier register(final String registryName, final String displayName, final int tierColor) {
		return this.delegate.register(registryName, displayName, tierColor, null);
	}
}
