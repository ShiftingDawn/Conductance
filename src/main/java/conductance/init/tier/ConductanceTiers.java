package conductance.init.tier;

import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.tier.event.RegisterTierEvent;
import conductance.Conductance;
import static net.minecraft.ChatFormatting.DARK_BLUE;
import static net.minecraft.ChatFormatting.DARK_GREEN;
import static net.minecraft.ChatFormatting.GOLD;
import static net.minecraft.ChatFormatting.YELLOW;
import static conductance.api.NCTiers.EV;
import static conductance.api.NCTiers.HV;
import static conductance.api.NCTiers.LV;
import static conductance.api.NCTiers.MV;

@ConductancePluginListener(modid = Conductance.MODID)
final class ConductanceTiers {

	@EventListener(priority = -100)
	private static void init(final RegisterTierEvent event) {
		LV = event.register("lv", DARK_BLUE + "LV", 0x004fff, LvComponentMap::new);
		MV = event.register("mv", GOLD + "MV", 0xbf6a40, MvComponentMap::new);
		HV = event.register("hv", YELLOW + "HV", 0xbfa640, HvComponentMap::new);
		EV = event.register("ev", DARK_GREEN + "EV", 0x237070, EvComponentMap::new);
	}

	private ConductanceTiers() {
	}
}
