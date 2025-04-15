package conductance.api.util.tier;

import java.util.List;

public interface TierRegistry {

	Tier empty();

	Tier max();

	Tier getTierByVoltage(long voltage);

	/**
	 * @return all registered tiers, sorted from lowest to highest
	 */
	List<Tier> getTiers();
}
