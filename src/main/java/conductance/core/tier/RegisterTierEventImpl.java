package conductance.core.tier;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredComponentMap;
import conductance.api.tier.event.RegisterTierEvent;

@RequiredArgsConstructor
final class RegisterTierEventImpl implements RegisterTierEvent {

	interface Delegate {
		Tier apply(String registryName, int tierColor, Supplier<TieredComponentMap> componentMapFactory, @Nullable Tier previousTier);
	}

	private final Delegate delegate;

	@Override
	public Tier register(final String registryName, final int tierColor, final Supplier<TieredComponentMap> componentMapFactory, final Tier previousTier) {
		if (!(previousTier instanceof TierImpl)) {
			throw new IllegalArgumentException("Invalid previousTier supplied. All tiers types MUST be created using %s".formatted(RegisterTierEvent.class.getSimpleName()));
		}
		return this.delegate.apply(registryName, tierColor, componentMapFactory, previousTier);
	}

	@Override
	public Tier register(final String registryName, final int tierColor, final Supplier<TieredComponentMap> componentMapFactory) {
		return this.delegate.apply(registryName, tierColor, componentMapFactory, null);
	}
}
