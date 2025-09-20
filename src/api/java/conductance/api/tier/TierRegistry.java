package conductance.api.tier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface TierRegistry {

	Tier empty();

	Tier max();

	Tier getByVoltage(long voltage);

	/**
	 * @return all registered tiers, sorted from lowest to highest
	 */
	List<Tier> getTiers();

	/**
	 * Create an immutable sorted map where every tier contains a value given by <code>factory</code>.
	 *
	 * @param factory function to map value to tier
	 * @param <T>     anything
	 * @return an immutable map populated by <code>factory</code> sorted by tier from lowest to highest
	 */
	<T> Map<Tier, T> newMap(Function<Tier, T> factory);
}
