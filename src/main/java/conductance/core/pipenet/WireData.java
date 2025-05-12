package conductance.core.pipenet;

import conductance.api.CAPI;
import conductance.api.util.tier.Tier;

public record WireData(long voltage, int amperage) {

	public Tier getTier() {
		return CAPI.tiers().getTierByVoltage(this.voltage);
	}
}
