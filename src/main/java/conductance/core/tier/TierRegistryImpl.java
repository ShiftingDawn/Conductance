package conductance.core.tier;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import conductance.api.tier.Tier;
import conductance.api.tier.TierRegistry;
import conductance.Conductance;

final class TierRegistryImpl implements TierRegistry {

	private static final Cache<Long, Tier> TIER_BY_VOLTAGE_CACHE = CacheBuilder.newBuilder().maximumSize(512).build();
	public static final TierRegistryImpl INSTANCE = new TierRegistryImpl();
	public static final String ID_EMPTY = "empty";
	public static final String ID_MAX = "max";
	private final Tier empty = new TierImpl(TierRegistryImpl.ID_EMPTY, ChatFormatting.BOLD + "EMPTY", -1, null);
	private final Tier max = new TierImpl(TierRegistryImpl.ID_MAX, ChatFormatting.RED.toString() + ChatFormatting.BOLD + "MAX", -1, this.empty);
	private final LinkedList<TierImpl> tiers = new LinkedList<>();
	private final AtomicBoolean frozen = new AtomicBoolean();

	@Override
	public Tier empty() {
		return this.empty;
	}

	@Override
	public Tier max() {
		return this.max;
	}

	@Override
	public Tier getTierByVoltage(final long voltage) {
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
	public List<Tier> getTiers() {
		return ImmutableList.copyOf(this.tiers);
	}

	@Override
	public <T> Map<Tier, T> newMap(final Function<Tier, T> factory) {
		return Collections.unmodifiableMap(Util.make(new IdentityHashMap<>(), map -> {
			for (final Tier tier : this.getTiers()) {
				map.put(tier, factory.apply(tier));
			}
		}));
	}

	void insertTier(final TierImpl tier) {
		if (this.frozen.get()) {
			throw new IllegalStateException("Trying to create new tier after TierRegistry has been frozen!");
		}
		if (tier.getRegistryKey().equals(TierRegistryImpl.ID_EMPTY) || tier.getRegistryKey().equals(TierRegistryImpl.ID_MAX)) {
			return;
		}
		if (tier.getPrevTier().getRegistryKey().equals(TierRegistryImpl.ID_MAX)) {
			throw new IllegalStateException("Cannot register tier %s with MAX as previous tier!".formatted(tier.getRegistryKey()));
		}
		if (this.tiers.isEmpty()) {
			tier.setPrevTier(null);
			tier.setNextTier(null);
			this.tiers.add(tier);
		} else if (tier.getPrevTier().isEmpty()) {
			tier.setPrevTier(null);
			tier.setNextTier(this.tiers.getFirst());
			this.tiers.getFirst().setPrevTier(tier);
			this.tiers.offerFirst(tier);
		} else {
			final Optional<TierImpl> existingTier = this.tiers.stream().filter(existing -> existing.getPrevTier() == tier.getPrevTier()).findFirst();
			if (existingTier.isPresent()) {
				final TierImpl oldTier = existingTier.get();
				final int index = this.tiers.indexOf(oldTier);
				if (!oldTier.getPrevTier().isEmpty()) {
					((TierImpl) oldTier.getPrevTier()).setNextTier(tier);
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

	@SuppressWarnings("SuspiciousMethodCalls")
	int getIndex(final Tier tier) {
		return this.tiers.indexOf(tier);
	}

	public Tier getLastTier() {
		return !this.tiers.isEmpty() ? this.tiers.getLast() : this.empty();
	}

	public void freeze() {
		Conductance.LOGGER.info("TierRegistry has been frozen!");
		this.frozen.set(true);
	}
}
