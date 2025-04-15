package conductance.api.machine;

import conductance.api.NCTiers;
import conductance.api.util.tier.Tier;

public interface IOverclockable {

	Tier getOverclockTier();

	void setOverclockTier(Tier tier);

	default Tier getMinOverclockTier() {
		return NCTiers.LV;
	}

	Tier getMaxOverclockTier();

	default long getOverclockVoltage() {
		return this.getOverclockTier().getVoltage();
	}
}
