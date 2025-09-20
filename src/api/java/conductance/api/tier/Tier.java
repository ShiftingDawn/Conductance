package conductance.api.tier;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;

public interface Tier {

	boolean isEmpty();

	boolean isMax();

	Tier getPreviousTier();

	Tier getNextTier();

	int getIndex();

	long getVoltage();

	default long getTierVoltage() {
		return (long) (this.getVoltage() * 0.8);
	}

	int getColor();

	TieredComponentMap getComponentMap();

	String getDescriptionId();

	Component getName();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().tiers().getKey(this), "Unregistered tier");
	}
}
