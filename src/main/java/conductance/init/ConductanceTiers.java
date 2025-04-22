package conductance.init;

import conductance.api.plugin.TierRegister;
import conductance.core.apiimpl.TierRegistryImpl;
import static conductance.api.NCTiers.EV;
import static conductance.api.NCTiers.HV;
import static conductance.api.NCTiers.LV;
import static conductance.api.NCTiers.MV;
import static net.minecraft.ChatFormatting.DARK_BLUE;
import static net.minecraft.ChatFormatting.DARK_GREEN;
import static net.minecraft.ChatFormatting.GOLD;
import static net.minecraft.ChatFormatting.YELLOW;

public final class ConductanceTiers {

	public static void init(final TierRegister register) {
		LV = register.register("lv", DARK_BLUE + "LV", 0x004fff).previous(TierRegistryImpl.EMPTY).build();
		MV = register.register("mv", GOLD + "MV", 0xbf6a40).build();
		HV = register.register("hv", YELLOW + "HV", 0xbfa640).build();
		EV = register.register("ev", DARK_GREEN + "EV", 0x237070).build();
	}

	private ConductanceTiers() {
	}
}
