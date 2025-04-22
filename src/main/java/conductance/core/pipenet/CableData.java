package conductance.core.pipenet;

import conductance.api.CAPI;
import conductance.api.util.tier.Tier;

public record CableData(long voltage, int amperage, int cableLoss, boolean superconductor) {

	public Tier getTier() {
		return CAPI.tiers().getTierByVoltage(this.voltage);
	}
}
