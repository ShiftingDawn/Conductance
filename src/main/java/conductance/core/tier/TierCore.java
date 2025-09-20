package conductance.core.tier;

import java.util.Objects;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import conductance.api.CAPI;
import conductance.api.tier.TierRegistry;
import conductance.api.tier.event.RegisterTierEvent;
import conductance.api.util.Lazy;
import conductance.Conductance;

public final class TierCore {

	public static void initialize() {
		Conductance.TIERS = Util.make(new TierRegistryImpl(), reg -> Conductance.setApiValue(TierRegistry.class, reg));

		Conductance.dispatch(RegisterTierEvent.class, modid -> new RegisterTierEventImpl(((registryName, tierColor, componentMapFactory, previousTier) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final TierImpl result = new TierImpl(ARGB.opaque(tierColor), Lazy.of(componentMapFactory));
			result.setPrevTier(Objects.requireNonNullElseGet(previousTier, Conductance.TIERS::getLastTier));
			Conductance.REGISTRIES.register(CAPI.regs().tiers(), registryKey, result);
			Conductance.TIERS.insertTier(result);
			return result;
		})));
	}

	private TierCore() {
	}
}
