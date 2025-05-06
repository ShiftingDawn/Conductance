package conductance.api.util.tier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface TierRegistry {

	Tier empty();

	Tier max();

	Tier getTierByVoltage(long voltage);

	/**
	 * @return all registered tiers, sorted from lowest to highest
	 */
	List<Tier> getTiers();

	<T> Map<Tier, T> newMap(Function<Tier, T> factory);
}
