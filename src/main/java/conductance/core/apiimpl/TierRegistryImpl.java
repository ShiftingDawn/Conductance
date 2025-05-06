package conductance.core.apiimpl;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import conductance.api.util.tier.Tier;
import conductance.api.util.tier.TierRegistry;
import conductance.Conductance;

public class TierRegistryImpl implements TierRegistry {

	private static final Cache<Long, Tier> TIER_BY_VOLTAGE_CACHE = CacheBuilder.newBuilder().maximumSize(512).build();
	public static final TierRegistryImpl INSTANCE = new TierRegistryImpl();
	public static final String ID_EMPTY = "empty";
	public static final String ID_MAX = "max";
	public static final Tier EMPTY = new TierImpl(TierRegistryImpl.ID_EMPTY, ChatFormatting.BOLD + "EMPTY", null);
	public static final Tier MAX = new TierImpl(TierRegistryImpl.ID_MAX, ChatFormatting.RED.toString() + ChatFormatting.BOLD + "MAX", TierRegistryImpl.EMPTY);
	private static final LinkedList<TierImpl> TIERS = new LinkedList<>();
	private static boolean frozen = false;

	@Override
	public Tier empty() {
		return TierRegistryImpl.EMPTY;
	}

	@Override
	public Tier max() {
		return TierRegistryImpl.MAX;
	}

	@Override
	public Tier getTierByVoltage(final long voltage) {
		try {
			return TierRegistryImpl.TIER_BY_VOLTAGE_CACHE.get(voltage, () -> {
				for (int i = 0; i < TierRegistryImpl.TIERS.size(); ++i) {
					final Tier tier = TierRegistryImpl.TIERS.get(i);
					if (voltage <= tier.getVoltage()) {
						return tier;
					}
				}
				return this.max();
			});
		} catch (final ExecutionException e) {
			//Should not happen, famous last words
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Tier> getTiers() {
		return ImmutableList.copyOf(TierRegistryImpl.TIERS);
	}

	@Override
	public <T> Map<Tier, T> newMap(final Function<Tier, T> factory) {
		return Collections.unmodifiableMap(Util.make(new IdentityHashMap<>(), map -> {
			for (final Tier tier : this.getTiers()) {
				map.put(tier, factory.apply(tier));
			}
		}));
	}

	static void insertTier(final TierImpl tier) {
		if (TierRegistryImpl.frozen) {
			throw new IllegalStateException("Trying to create new tier after TierRegistry has been frozen!");
		}
		if (tier.getRegistryKey().equals(TierRegistryImpl.ID_EMPTY) || tier.getRegistryKey().equals(TierRegistryImpl.ID_MAX)) {
			return;
		}
		if (tier.getPrevTier() == TierRegistryImpl.MAX) {
			throw new IllegalStateException("Cannot register tier %s with MAX as previous tier!".formatted(tier.getRegistryKey()));
		}
		if (TierRegistryImpl.TIERS.isEmpty()) {
			tier.setPrevTier(null);
			tier.setNextTier(null);
			TierRegistryImpl.TIERS.add(tier);
		} else if (tier.getPrevTier().isEmpty()) {
			tier.setPrevTier(null);
			tier.setNextTier(TierRegistryImpl.TIERS.getFirst());
			TierRegistryImpl.TIERS.getFirst().setPrevTier(tier);
			TierRegistryImpl.TIERS.offerFirst(tier);
		} else {
			final Optional<TierImpl> existingTier = TierRegistryImpl.TIERS.stream().filter(existing -> existing.getPrevTier() == tier.getPrevTier()).findFirst();
			if (existingTier.isPresent()) {
				final TierImpl oldTier = existingTier.get();
				final int index = TierRegistryImpl.TIERS.indexOf(oldTier);
				if (!oldTier.getPrevTier().isEmpty()) {
					((TierImpl) oldTier.getPrevTier()).setNextTier(tier);
				}
				oldTier.setPrevTier(tier);
				tier.setNextTier(oldTier);
				TierRegistryImpl.TIERS.add(index, tier);
			} else {
				TierRegistryImpl.TIERS.getLast().setNextTier(tier);
				tier.setPrevTier(TierRegistryImpl.TIERS.getLast());
				TierRegistryImpl.TIERS.add(tier);
			}
		}
		TierRegistryImpl.TIERS.forEach(TierImpl::recalculate);
	}

	static void removeTier(final TierImpl tier) {
		if (!TierRegistryImpl.TIERS.contains(tier)) {
			return;
		}
		if (tier.getPrevTier() != TierRegistryImpl.EMPTY) {
			((TierImpl) tier.getPrevTier()).setNextTier(tier.getNextTier());
		}
		if (tier.getNextTier() != TierRegistryImpl.MAX) {
			((TierImpl) tier.getNextTier()).setPrevTier(tier.getPrevTier());
		}
		TierRegistryImpl.TIERS.remove(tier);
		TierRegistryImpl.TIERS.forEach(TierImpl::recalculate);
	}

	static int getIndex(final Tier tier) {
		return TierRegistryImpl.TIERS.indexOf(tier);
	}

	public static Tier getLastTier() {
		return !TierRegistryImpl.TIERS.isEmpty() ? TierRegistryImpl.TIERS.getLast() : TierRegistryImpl.EMPTY;
	}

	public static void freeze() {
		Conductance.LOGGER.info("TierRegistry has been frozen!");
		TierRegistryImpl.frozen = true;
	}
}
