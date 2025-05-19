package conductance.init;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.plugin.RegisterTierEvent;
import conductance.Conductance;
import conductance.core.apiimpl.TierRegistryImpl;
import static net.minecraft.ChatFormatting.DARK_BLUE;
import static net.minecraft.ChatFormatting.DARK_GREEN;
import static net.minecraft.ChatFormatting.GOLD;
import static net.minecraft.ChatFormatting.YELLOW;
import static conductance.api.NCTiers.EV;
import static conductance.api.NCTiers.HV;
import static conductance.api.NCTiers.LV;
import static conductance.api.NCTiers.MV;

@ConductancePluginListener(modid = Conductance.MODID)
public final class ConductanceTiers {

	@EventListener(priority = -100)
	private static void init(final RegisterTierEvent event) {
		LV = event.register("lv", DARK_BLUE + "LV", 0x004fff, TierRegistryImpl.EMPTY);
		MV = event.register("mv", GOLD + "MV", 0xbf6a40);
		HV = event.register("hv", YELLOW + "HV", 0xbfa640);
		EV = event.register("ev", DARK_GREEN + "EV", 0x237070);
	}

	private ConductanceTiers() {
	}
}
