package conductance.core.tier;

import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;
import conductance.api.tier.event.RegisterTierEvent;

@AllArgsConstructor
final class RegisterTierEventImpl implements RegisterTierEvent {

	public interface TierRegister {

		Tier register(String registryName, String displayName, int tierColor, Supplier<TieredComponentMap> componentMapFactory, @Nullable Tier previousTier);
	}

	private final TierRegister delegate;

	@Override
	public Tier register(final String registryName, final String displayName, final int tierColor, final Supplier<TieredComponentMap> componentMapFactory, final Tier previousTier) {
		return this.delegate.register(registryName, displayName, tierColor, componentMapFactory, previousTier);
	}

	@Override
	public Tier register(final String registryName, final String displayName, final int tierColor, final Supplier<TieredComponentMap> componentMapFactory) {
		return this.delegate.register(registryName, displayName, tierColor, componentMapFactory, null);
	}
}
