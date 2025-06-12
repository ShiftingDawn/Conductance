package conductance.core.tier;

import java.util.Objects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import conductance.api.tier.Tier;
import conductance.api.tier.TierRegistry;
import conductance.api.tier.event.RegisterTierEvent;
import conductance.Conductance;
import static conductance.core.register.RegisterCore.REGS;

public final class TierCore {

	public static void initialize(final IEventBus modEventBus) {
		Conductance.setApiValue(TierRegistry.class, TierRegistryImpl.INSTANCE);
		modEventBus.addListener(FMLLoadCompleteEvent.class, ignored -> TierRegistryImpl.INSTANCE.freeze());

		REGS.tiers().setRegisterCallback((id, tier) -> TierRegistryImpl.INSTANCE.insertTier((TierImpl) tier));

		Conductance.dispatchAll(RegisterTierEvent.class, new RegisterTierEventImpl((registryName, displayName, tierColor, componentMapFactory, previousTier) -> {
			final Tier result = new TierImpl(registryName, displayName, 0xFF000000 | tierColor, componentMapFactory, Objects.requireNonNullElseGet(previousTier, TierRegistryImpl.INSTANCE::getLastTier));
			REGS.tiers().register(result);
			return result;
		}));
	}

	private TierCore() {
	}
}
