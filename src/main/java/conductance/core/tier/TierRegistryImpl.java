package conductance.core.tier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import net.minecraft.Util;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import conductance.api.tier.Tier;
import conductance.api.tier.TierRegistry;
import conductance.api.util.Lazy;

public final class TierRegistryImpl implements TierRegistry {

	private static final Cache<Long, Tier> TIER_BY_VOLTAGE_CACHE = CacheBuilder.newBuilder().maximumSize(512).build();
	private final Tier empty = new TierImpl(-1, Lazy.of(EmptyTieredComponentMap.INSTANCE));
	private final Tier max = new TierImpl(-1, Lazy.of(EmptyTieredComponentMap.INSTANCE));
	private final LinkedList<TierImpl> tiers = new LinkedList<>();
	private final List<Tier> unmodifiableList = Collections.unmodifiableList(this.tiers);

	void insertTier(final TierImpl tier) {
		if (tier.isEmpty() || tier.isMax()) {
			return;
		}
		if (tier.getPreviousTier().isMax()) {
			throw new IllegalStateException("Cannot register tier %s with MAX as previous tier!".formatted(tier.getId()));
		}
		if (this.tiers.isEmpty()) {
			tier.setPrevTier(null);
			tier.setNextTier(null);
			this.tiers.add(tier);
		} else if (tier.getPreviousTier().isEmpty()) {
			tier.setPrevTier(null);
			tier.setNextTier(this.tiers.getFirst());
			this.tiers.getFirst().setPrevTier(tier);
			this.tiers.offerFirst(tier);
		} else {
			final Optional<TierImpl> existingTier = this.tiers.stream().filter(existing -> existing.getPreviousTier() == tier.getPreviousTier()).findFirst();
			if (existingTier.isPresent()) {
				final TierImpl oldTier = existingTier.get();
				final int index = this.tiers.indexOf(oldTier);
				if (!oldTier.getPreviousTier().isEmpty()) {
					((TierImpl) oldTier.getPreviousTier()).setNextTier(tier);
				}
				oldTier.setPrevTier(tier);
				tier.setNextTier(oldTier);
				this.tiers.add(index, tier);
			} else {
				this.tiers.getLast().setNextTier(tier);
				tier.setPrevTier(this.tiers.getLast());
				this.tiers.add(tier);
			}
		}
		this.tiers.forEach(TierImpl::recalculate);
	}

	int getIndex(final TierImpl tier) {
		return this.tiers.indexOf(tier);
	}

	@Override
	public Tier empty() {
		return this.empty;
	}

	@Override
	public Tier max() {
		return this.max;
	}

	@Override
	public Tier getByVoltage(final long voltage) {
		try {
			return TierRegistryImpl.TIER_BY_VOLTAGE_CACHE.get(voltage, () -> {
				for (final Tier tier : this.tiers) {
					if (voltage <= tier.getVoltage()) {
						return tier;
					}
				}
				return this.max();
			});
		} catch (final ExecutionException e) {
			//Should not happen, famous last words
			throw new AssertionError(e);
		}
	}

	@Override
	public Tier getByVoltageFloored(final long voltage) {
		final Tier tier = this.getByVoltage(voltage);
		return voltage < tier.getVoltage() ? tier.getPreviousTier() : tier;
	}

	public Tier getLastTier() {
		return !this.tiers.isEmpty() ? this.tiers.getLast() : this.empty();
	}

	@Override
	public List<Tier> getTiers() {
		return this.unmodifiableList;
	}

	@Override
	public <T> Map<Tier, T> newMap(final Function<Tier, T> factory) {
		return Collections.unmodifiableMap(Util.make(new LinkedHashMap<>(), map -> {
			for (final Tier tier : this.tiers) {
				map.put(tier, factory.apply(tier));
			}
		}));
	}
}
