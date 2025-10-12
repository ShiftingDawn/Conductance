package conductance.lib.pipenet;

import conductance.api.CAPI;
import conductance.api.tier.Tier;

public record WireData(long voltage, int amperage) {

	public Tier getTier() {
		return CAPI.tiers().getByVoltage(this.voltage);
	}
}
